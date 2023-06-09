package net.deechael.ktkook.command

import snw.jkook.command.CommandSender
import snw.jkook.command.ConsoleCommandSender
import snw.jkook.command.JKookCommand
import snw.jkook.entity.User
import snw.jkook.message.Message

class KtCommand(val name: String) {

    internal val nodes: MutableList<KtCommand> = mutableListOf()

    var original: JKookCommand

    init {
        this.original = JKookCommand(name)
    }

    fun toJKook(): JKookCommand {
        for (node in this.nodes) {
            this.original.addSubcommand(node.toJKook())
        }
        val cache = this.original
        this.original = JKookCommand(name)
        return cache
    }

}

class KtArgument(val parent: KtCommand, val type: Class<Any?>) {

}

class KtOptionalArgument<T>(val parent: KtCommand, val type: Class<T>, val defaultValue: T) {

}

fun Literal(name: String, content: KtCommand.() -> Unit): KtCommand {
    val command = KtCommand(name)
    command.apply(content)
    return command
}

fun KtCommand.Literal(name: String, content: KtCommand.() -> Unit) {
    val node = KtCommand(name)
    node.apply(content)
    this.nodes.add(node)
}

fun KtArgument.Literal(name: String, content: KtCommand.() -> Unit) {
    val node = KtCommand(name)
    node.apply(content)
    this.parent.nodes.add(node)
}

fun KtOptionalArgument<Any?>.Literal(name: String, content: KtCommand.() -> Unit) {
    val node = KtCommand(name)
    node.apply(content)
    this.parent.nodes.add(node)
}

fun KtCommand.Argument(type: Class<Any?>, content: KtArgument.() -> Unit) {
    val node = KtArgument(this, type)
    node.apply(content)
    this.original.addArgument(type)
}

fun KtArgument.Argument(type: Class<Any?>, content: KtArgument.() -> Unit) {
    val node = KtArgument(this.parent, type)
    node.apply(content)
    this.parent.original.addArgument(type)
}

fun KtOptionalArgument<Any?>.Argument(type: Class<Any?>, content: KtArgument.() -> Unit) {
    val node = KtArgument(this.parent, type)
    node.apply(content)
    this.parent.original.addArgument(type)
}

fun <T> KtCommand.OptionalArgument(type: Class<T>, defaultValue: T, content: KtOptionalArgument<T>.() -> Unit) {
    val node = KtOptionalArgument(this, type, defaultValue)
    node.apply(content)
    node.parent.original.addOptionalArgument(type, defaultValue)
}

fun <T> KtArgument.OptionalArgument(type: Class<T>, defaultValue: T, content: KtOptionalArgument<T>.() -> Unit) {
    val node = KtOptionalArgument(this.parent, type, defaultValue)
    node.apply(content)
    node.parent.original.addOptionalArgument(type, defaultValue)
}

fun <T> KtOptionalArgument<Any?>.OptionalArgument(type: Class<T>, defaultValue: T, content: KtOptionalArgument<T>.() -> Unit) {
    val node = KtOptionalArgument(this.parent, type, defaultValue)
    node.apply(content)
    node.parent.original.addOptionalArgument(type, defaultValue)
}

fun KtCommand.Executor(content: (sender: CommandSender, args: Array<Any>, message: Message?) -> Unit) {
    this.original.setExecutor(content)
}

fun KtArgument.Executor(content: (sender: CommandSender, args: Array<Any>, message: Message?) -> Unit) {
    this.parent.original.setExecutor(content)
}

fun KtOptionalArgument<Any?>.Executor(content: (sender: CommandSender, args: Array<Any>, message: Message?) -> Unit) {
    this.parent.original.setExecutor(content)
}

fun KtCommand.UserExecutor(content: (sender: User, args: Array<Any>, message: Message?) -> Unit) {
    this.original.executesUser(content)
}

fun KtArgument.UserExecutor(content: (sender: User, args: Array<Any>, message: Message?) -> Unit) {
    this.parent.original.executesUser(content)
}

fun KtOptionalArgument<Any?>.UserExecutor(content: (sender: User, args: Array<Any>, message: Message?) -> Unit) {
    this.parent.original.executesUser(content)
}

fun KtCommand.ConsoleExecutor(content: (sender: ConsoleCommandSender, args: Array<Any>) -> Unit) {
    this.original.executesConsole(content)
}

fun KtArgument.ConsoleExecutor(content: (sender: ConsoleCommandSender, args: Array<Any>) -> Unit) {
    this.parent.original.executesConsole(content)
}

fun KtOptionalArgument<Any?>.ConsoleExecutor(content: (sender: ConsoleCommandSender, args: Array<Any>) -> Unit) {
    this.parent.original.executesConsole(content)
}