package net.deechael.ktkook.internel

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import snw.jkook.Core
import snw.jkook.plugin.InvalidPluginException
import snw.jkook.plugin.Plugin
import snw.jkook.plugin.PluginClassLoader
import snw.jkook.plugin.PluginDescription
import snw.kookbc.impl.KBCClient
import snw.kookbc.impl.plugin.PrefixLogger
import snw.kookbc.impl.plugin.SimplePluginClassLoader
import snw.kookbc.util.Util
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarFile
import kotlin.reflect.KClass

class KtPluginLoader(private val client: KBCClient, parent: ClassLoader) : PluginClassLoader(arrayOf<URL>(), parent) {

    companion object {

        @JvmStatic
        val INSTANCES: MutableCollection<KtPluginLoader> = Collections.newSetFromMap(WeakHashMap())

        @JvmStatic
        val KT_PLUGIN_METADATA_ENTRY: String = "kt-plugin.yml"

    }

    private var description: PluginDescription? = null

    init {
        INSTANCES.add(this)
    }

    private fun initMixins(file: File) {
        val confNameSet: MutableSet<String> = HashSet()
        try {
            JarFile(file).use { jarFile ->
                val enumeration = jarFile.entries()
                while (enumeration.hasMoreElements()) {
                    val jarEntry = enumeration.nextElement()
                    val name = jarEntry.name
                    if (name.startsWith("mixin.") && name.endsWith(".json")) {
                        confNameSet.add(name)
                    }
                }
            }
        } catch (e: IOException) {
            throw InvalidPluginException(e)
        }
        if (confNameSet.isNotEmpty()) {
            if (!Util.isStartByLaunch()) {
                client.core.logger.warn(
                    "[{}] {} v{} plugin is using the Mixin framework. Please use 'Launch' mode to enable support for Mixin",
                    description?.name,
                    description?.name,
                    description?.version
                )
                return
            }
            try {
                for (name in confNameSet) {
                    JarFile(file).use { jarFile ->
                        val zipEntry = jarFile.getEntry(name)
                        client.pluginMixinConfigManager.add(description, name, jarFile.getInputStream(zipEntry))
                    }
                }
            } catch (e: IOException) {
                throw InvalidPluginException(e)
            }
        }
    }

    override fun lookForMainClass(mainClassName: String, file: File): Class<out Plugin> {
        initMixins(file)
        return super.lookForMainClass(mainClassName, file)
    }


    override fun loadPlugin0(file: File): Plugin {
        beforeOpen(file)

        // load the given file as JarFile
        JarFile(file).use { jar ->  // try-with-resources!
            // try to find plugin.yml
            val entry =
                jar.getJarEntry(KT_PLUGIN_METADATA_ENTRY)
                    ?: // if not found
                    throw IllegalArgumentException("We cannot find $KT_PLUGIN_METADATA_ENTRY") // fail
            // or we should read the plugin.yml and parse it to get information
            val pluginYmlStream = jar.getInputStream(entry)

            // construct description
            val description = createDescription(pluginYmlStream)
            val mainClassName = description.mainClassName
            val main = lookForMainClass(mainClassName, file)
            return construct(main, description)
        }
    }

    @Throws(ClassNotFoundException::class)
    override fun findClass(name: String): Class<*> {
        return findClass0(name, false)
    }

    @Throws(ClassNotFoundException::class)
    fun findClass0(name: String, dontCallOther: Boolean): Class<*> {
        try {
            return super.findClass(name)
        } catch (ignored: ClassNotFoundException) {
        }

        // Try to load class from other known instances if needed
        if (!dontCallOther) {
            return loadFromOther(name)
        }
        throw ClassNotFoundException(name)
    }

    @Throws(ClassNotFoundException::class)
    protected fun loadFromOther(name: String): Class<*> {
        for (classLoader in SimplePluginClassLoader.INSTANCES) {
            if (classLoader == null) {
                continue
            }
            try {
                return classLoader.findClass0(name, true)
            } catch (ignored: ClassNotFoundException) {
                throw ignored
            }
        }
        for (classLoader in INSTANCES) {
            if (classLoader === this) {
                continue
            }
            try {
                return classLoader.findClass0(name, true)
            } catch (ignored: ClassNotFoundException) {
                throw ignored
            }
        }
        throw ClassNotFoundException(name)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Plugin?> construct(cls: Class<T>?, description: PluginDescription?): T {
        val plugin = cls!!.getDeclaredField("INSTANCE").get(null)
        val initMethod = cls.getMethod(
            "init",
            File::class.java,
            File::class.java,
            PluginDescription::class.java,
            File::class.java,
            Logger::class.java,
            Core::class.java
        )
        var clazzURI = cls.protectionDomain.codeSource.location.toURI().rawSchemeSpecificPart
        val endIndex = clazzURI!!.length - (cls.name.replace(".", "/") + ".class").length - 2
        clazzURI = clazzURI.substring(0, endIndex)
        val pluginFile = File(URI(clazzURI).path)
        val dataFolder = File(client.pluginsFolder, description!!.name)
        initMethod.invoke(
            plugin,
            File(dataFolder, "config.yml"),
            dataFolder,
            description,
            pluginFile,
            PrefixLogger(description.name, LoggerFactory.getLogger(cls)),
            client.core
        )
        return plugin as T
    }

    @Throws(IOException::class)
    override fun close() {
        INSTANCES.remove(this)
        super.close()
    }

}