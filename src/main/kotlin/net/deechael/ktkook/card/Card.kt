package net.deechael.ktkook.card

import snw.jkook.message.component.card.CardComponent
import snw.jkook.message.component.card.MultipleCardComponent
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.module.BaseModule

class MultipleCardBuilder {

    val cards: MutableList<CardComponent> = mutableListOf()

    fun CardComponent(theme: Theme = Theme.NONE, size: Size = Size.LG, color: String? = null, content: CardBuilder.() -> Unit = {}) {
        val builder = CardBuilder()
        builder.theme = theme
        builder.size = size
        builder.color = color
        builder.apply(content)
        this.cards.add(builder.build())
    }

    fun build(): MultipleCardComponent {
        return MultipleCardComponent(this.cards.toList())
    }

}

class CardBuilder {

    var theme: Theme = Theme.NONE
    var size: Size = Size.LG
    var color: String? = null
    val modules: MutableList<BaseModule> = mutableListOf()

    fun build(): CardComponent {
        return CardComponent(this.modules, this.size, this.theme, this.color)
    }

}

fun MultipleCardComponent(content: MultipleCardBuilder.() -> Unit): MultipleCardComponent {
    val builder = MultipleCardBuilder()
    builder.apply(content)
    return builder.build()
}

fun CardComponent(theme: Theme = Theme.NONE, size: Size = Size.LG, color: String? = null, content: CardBuilder.() -> Unit = {}): CardComponent {
    val builder = CardBuilder()
    builder.theme = theme
    builder.size = size
    builder.color = color
    builder.apply(content)
    return builder.build()
}