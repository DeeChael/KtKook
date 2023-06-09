package net.deechael.ktkook.card

import snw.jkook.entity.abilities.Accessory
import snw.jkook.message.component.FileComponent
import snw.jkook.message.component.card.CardScopeElement
import snw.jkook.message.component.card.Size
import snw.jkook.message.component.card.Theme
import snw.jkook.message.component.card.element.*
import snw.jkook.message.component.card.module.*
import snw.jkook.message.component.card.module.CountdownModule.Type

class ActionGroupBuilder {

    private val buttons: MutableList<InteractElement> = mutableListOf()

    fun Button(theme: Theme = Theme.PRIMARY, value: String = "", type: ButtonElement.EventType = ButtonElement.EventType.NO_ACTION, content: ButtonBuilder.() -> Unit = {}) {
        val builder = ButtonBuilder()
        builder.theme = theme
        builder.value = value
        builder.type = type
        builder.apply(content)
        this.buttons.add(builder.build())
    }

    fun build(): ActionGroupModule {
        return ActionGroupModule(this.buttons)
    }

}

fun CardBuilder.ActionGroup(content: ActionGroupBuilder.() -> Unit = {}): ActionGroupModule {
    val builder = ActionGroupBuilder()
    builder.apply(content)
    val module = builder.build()
    this.modules.add(module)
    return module
}

class ContainerBuilder {

    private val images: MutableList<ImageElement> = mutableListOf()

    fun Image(src: String, alt: String? = null, size: Size? = null, circle: Boolean = false) {
        this.images.add(ImageElement(src, alt, size, circle))
    }

    fun build(): ContainerModule {
        return ContainerModule(this.images)
    }

}

fun CardBuilder.Container(content: ContainerBuilder.() -> Unit = {}): ContainerModule {
    val builder = ContainerBuilder()
    builder.apply(content)
    val module = builder.build()
    this.modules.add(module)
    return module
}

class ContextBuilder {

    private val elements: MutableList<BaseElement> = mutableListOf()

    fun Text(content: String, kmarkdown: Boolean = false, emoji: Boolean = false) {
        if (kmarkdown) {
            this.elements.add(MarkdownElement(content))
        } else {
            this.elements.add(PlainTextElement(content, emoji))
        }
    }

    fun build(): ContextModule {
        return ContextModule(this.elements)
    }

}

fun CardBuilder.Context(content: ContextBuilder.() -> Unit = {}): ContextModule {
    val builder = ContextBuilder()
    builder.apply(content)
    val module = builder.build()
    this.modules.add(module)
    return module
}

fun CardBuilder.Countdown(type: Type, endTime: Long): CountdownModule {
    val module = CountdownModule(type, endTime)
    this.modules.add(module)
    return module
}

fun CardBuilder.Divider(): DividerModule {
    this.modules.add(DividerModule.INSTANCE)
    return DividerModule.INSTANCE
}


fun CardBuilder.File(type: FileComponent.Type, src: String, title: String, cover: String? = null): FileModule {
    val module = FileModule(type, src, title, cover)
    this.modules.add(module)
    return module
}

class HeaderBuilder {

    private var text: PlainTextElement = PlainTextElement("")

    fun Text(content: String, emoji: Boolean = false) {
        this.text = PlainTextElement(content, emoji)
    }

    fun build(): HeaderModule {
        return HeaderModule(this.text)
    }

}

fun CardBuilder.Header(content: HeaderBuilder.() -> Unit = {}): HeaderModule {
    val builder = HeaderBuilder()
    builder.apply(content)
    val module = builder.build()
    this.modules.add(module)
    return module
}

class ImageGroupBuilder {

    private val images: MutableList<ImageElement> = mutableListOf()

    fun Image(src: String, alt: String? = null, size: Size? = null, circle: Boolean = false) {
        this.images.add(ImageElement(src, alt, size, circle))
    }

    fun build(): ImageGroupModule {
        return ImageGroupModule(this.images)
    }

}

fun CardBuilder.ImageGroup(content: ImageGroupBuilder.() -> Unit = {}): ImageGroupModule {
    val builder = ImageGroupBuilder()
    builder.apply(content)
    val module = builder.build()
    this.modules.add(module)
    return module
}

fun CardBuilder.Invite(code: String): InviteModule {
    val module = InviteModule(code)
    this.modules.add(module)
    return module
}

class SectionBuilder {

    var text: CardScopeElement = PlainTextElement("")
    var accessory: Accessory? = null
    var mode: Accessory.Mode? = null

    fun Text(content: String, kmarkdown: Boolean = false, emoji: Boolean = false) {
        if (kmarkdown) {
            this.text = MarkdownElement(content)
        } else {
            this.text = PlainTextElement(content, emoji)
        }
    }

    fun Paragraph(columns: Int = 0, content: ParagraphBuilder.() -> Unit = {}) {
        val builder = ParagraphBuilder()
        builder.columns = columns
        builder.apply(content)
        this.text = builder.build()
    }

    fun Button(theme: Theme = Theme.PRIMARY, value: String = "", type: ButtonElement.EventType = ButtonElement.EventType.NO_ACTION, content: ButtonBuilder.() -> Unit = {}) {
        val builder = ButtonBuilder()
        builder.theme = theme
        builder.value = value
        builder.type = type
        builder.apply(content)
        this.mode = Accessory.Mode.RIGHT
        this.accessory = builder.build()
    }

    fun Image(src: String, alt: String? = null, size: Size? = null, circle: Boolean = false) {
        this.accessory = ImageElement(src, alt, size, circle)
    }

    fun build(): SectionModule {
        return SectionModule(text, accessory, mode)
    }

}

fun CardBuilder.Section(mode: Accessory.Mode? = null, content: SectionBuilder.() -> Unit = {}): SectionModule {
    val builder = SectionBuilder()
    builder.mode = mode
    builder.apply(content)
    val module = builder.build()
    this.modules.add(module)
    return module
}
