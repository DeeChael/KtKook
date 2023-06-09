# KtKook
为KookBC提供了Kotlin高级功能
## 快速上手
**!!** 想要使用本插件的功能，请使用Kotlin进行开发，并将插件中的 `plugin.yml` 重命名为 `kt-plugin.yml`，并在依赖项中标明KtKook
```yaml
name: Example
version: 1.0.0
api-version: 0.49.0
authors: ["DeeChael"]
main: net.deechael.example.ExampleKtPlugin
depend: ["KtKook"]
```
首先，本插件提供了对Kotlin的object class的支持：
```kotlin
object ExampleKtPlugin: KtPlugin() {

    override fun onEnable() {
        TODO("Not implemented yet!")
    }

}

// 如java一样的类继承的创建方法也是可以正常使用的的：
class ExampleKtPlugin(): KtPlugin() {

    override fun onEnable() {
        TODO("Not implemented yet!")
    }

}
```
同时对指令 & 卡片消息通过了 DSL 支持
```kotlin

object ExampleKtPlugin: KtPlugin() {

    override fun onEnable() {
        TODO("Not implemented yet!")
        // 可以通过 KtPlugin 中的 registerCommand(String) 方法直接访问创建指令并通过 DSL 进行开发
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

    // 创建一个卡片消息
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
```
以上代码的结果：
![机器人展示](./screenshots/example.png)