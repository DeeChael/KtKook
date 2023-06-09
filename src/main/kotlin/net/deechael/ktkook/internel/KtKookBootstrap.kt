package net.deechael.ktkook.internel

import net.deechael.ktkook.plugin.KtPlugin
import snw.jkook.JKook
import snw.jkook.plugin.PluginLoader
import snw.kookbc.impl.CoreImpl
import java.io.IOException
import java.util.jar.JarFile

class KtKookBootstrap: KtPlugin() {

    override fun onLoad() {
    }

    override fun onEnable() {
        JKook.getPluginManager().registerPluginLoader({ f ->
            if (!f.name.endsWith(".jar"))
                return@registerPluginLoader false
            val jarFile = JarFile(f)
            return@registerPluginLoader jarFile.getJarEntry("kt-plugin.yml") != null
        }, this::createKtPluginLoader)
        this.logger.info("=====================================")
        this.logger.info("=          LAUNCHED KTKOOK          =")
        this.logger.info("=  WELCOME TO THE WORLD OF KOTLIN!  =")
        this.logger.info("=====================================")
    }

    private fun createKtPluginLoader(parent: ClassLoader): PluginLoader {
        return KtPluginLoader((JKook.getCore() as CoreImpl).client, this.javaClass.classLoader)
    }

}