package cz.lukynka.omorphyx.demo

import cz.lukynka.bindables.Bindable
import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.container.Container
import cz.lukynka.omorphyx.renderer.graphics.debug.TextDrawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.shape.Box

class Button() : CompositeDrawable() {

    constructor(unit: Button.() -> Unit) : this() {
        unit.invoke(this)
    }

    val text = Bindable("Button")
    lateinit var textDrawable: TextDrawable

    init {
        text.valueChanged { event ->
            textDrawable.text = event.newValue
        }

        addChild(Container {
            relativeSizeAxes = Axes.BOTH
            anchor = Anchor.CENTER
            origin = Anchor.CENTER
            addChild(Box {
                relativeSizeAxes = Axes.BOTH
                color = Color4.PURPLE
                cornerRadius = 10f
            })
            textDrawable = addChild(TextDrawable {
                anchor = Anchor.CENTER
                origin = Anchor.CENTER
                fontSize = 24f
                text = this@Button.text.value
            })
        })
    }


}