package net.deechael.ktkook.plugin

import net.deechael.ktkook.command.KtCommand
import snw.jkook.JKook
import snw.jkook.plugin.BasePlugin

open class KtPlugin: BasePlugin() {

    fun registerCommand(name: String, content: KtCommand.() -> Unit) {
        val command = KtCommand(name)
        command.apply(content)
        JKook.getCommandManager().registerCommand(this, command.toJKook())
    }

}