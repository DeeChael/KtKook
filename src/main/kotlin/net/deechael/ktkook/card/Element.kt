package net.deechael.ktkook.card

import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.BaseElement
import snw.jkook.message.component.card.element.ButtonElement
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.element.PlainTextElement

class ButtonBuilder {

    var theme: Theme = Theme.PRIMARY
    var value: String = ""
    var type: ButtonElement.EventType = ButtonElement.EventType.NO_ACTION
    var element: BaseElement = PlainTextElement("Button")

    fun Text(content: String, kmarkdown: Boolean = false, emoji: Boolean = false) {
        if (kmarkdown) {
            this.element = MarkdownElement(content)
        } else {
            this.element = PlainTextElement(content, emoji)
        }
    }

    fun build(): ButtonElement {
        return ButtonElement(this.theme, this.value, this.type, this.element)
    }

}