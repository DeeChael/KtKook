package net.deechael.ktkook.card

import snw.jkook.message.component.card.element.BaseElement
import snw.jkook.message.component.card.element.MarkdownElement
import snw.jkook.message.component.card.element.PlainTextElement
import snw.jkook.message.component.card.structure.Paragraph

class ParagraphBuilder {

    var columns = 0
    var fields: MutableList<BaseElement> = mutableListOf()

    fun Text(content: String, kmarkdown: Boolean = false, emoji: Boolean = false) {
        if (kmarkdown) {
            this.fields.add(MarkdownElement(content))
        } else {
            this.fields.add(PlainTextElement(content, emoji))
        }
    }

    fun build(): Paragraph {
        return Paragraph(columns, fields)
    }

}