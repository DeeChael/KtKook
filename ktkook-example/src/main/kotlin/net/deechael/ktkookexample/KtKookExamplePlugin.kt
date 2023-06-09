package net.deechael.ktkookexample

import net.deechael.ktkook.card.ActionGroup
import net.deechael.ktkook.card.Context
import net.deechael.ktkook.card.Divider
import net.deechael.ktkook.card.Header
import net.deechael.ktkook.card.MultipleCardComponent
import net.deechael.ktkook.card.Section
import net.deechael.ktkook.command.Executor
import net.deechael.ktkook.command.Literal
import net.deechael.ktkook.command.UserExecutor
import net.deechael.ktkook.plugin.KtPlugin
import snw.jkook.message.component.card.MultipleCardComponent
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.ButtonElement
import snw.kookbc.util.GsonUtil

object KtKookExamplePlugin: KtPlugin() {

    override fun onEnable() {
        this.logger.info("Hello world!\nKtKook Plugin success!")

        this.registerCommand("test") {

            Literal("sub") {

                Executor { sender, args, message ->
                    message?.reply("You are in sub command")
                }

            }

            Literal("card") {
                UserExecutor { sender, args, message ->
                    val cardComponent = cardExample()
                    println(GsonUtil.CARD_GSON.toJson(cardComponent))
                    message?.reply(cardComponent)
                }
            }

            Executor { sender, args, message ->
                message?.reply("Hello, world!")
            }

        }
    }

    fun cardExample(): MultipleCardComponent {
        val multipleCardComponent = MultipleCardComponent {
            CardComponent {
                Header {
                    Text("Hello, world!")
                }
                Divider()
                Section {
                    Text(
                        content = "This is just a simple example for **KtKook** plugin",
                        kmarkdown = true
                    )
                }
                Section {
                    Text("Click the button right to see more!")
                    Button(
                        value = "https://www.github.com/DeeChael",
                        type = ButtonElement.EventType.LINK
                    ) {
                        Text("Go to Github!")
                    }
                }
                Section {
                    Paragraph(
                        columns = 3
                    ) {
                        // Line 1
                        Text(
                            content = "**Name**",
                            kmarkdown = true
                        )
                        Text(
                            content = "**Sex**",
                            kmarkdown = true
                        )
                        Text(
                            content = "**Age**",
                            kmarkdown = true
                        )

                        // Line 2
                        Text("DeeChael")
                        Text("Male")
                        Text("0")

                        // Line 3
                        Text("???")
                        Text("Female")
                        Text("???")
                    }
                }
                ActionGroup {
                    Button(
                        value = "first",
                        type = ButtonElement.EventType.RETURN_VAL
                    ) {
                        Text("OK")
                    }

                    Button(
                        value = "first",
                        theme = Theme.DANGER,
                        type = ButtonElement.EventType.RETURN_VAL
                    ) {
                        Text("NO")
                    }
                }
                Divider()
                Context {
                    Text("Powered by IntelliJ IDEA")
                }
            }
        }

        return multipleCardComponent
    }

}