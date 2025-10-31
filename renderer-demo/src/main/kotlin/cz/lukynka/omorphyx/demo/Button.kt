package cz.lukynka.omorphyx.demo

import cz.lukynka.bindables.Bindable
import cz.lukynka.kairos.Scheduler
import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.dependency.DependencyContainer
import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.container.Container
import cz.lukynka.omorphyx.renderer.graphics.debug.TextDrawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.layout.vector.Vector2f
import cz.lukynka.omorphyx.renderer.graphics.shape.Box
import cz.lukynka.omorphyx.renderer.transform.Easing
import java.awt.event.KeyEvent

class Button() : CompositeDrawable() {

    constructor(unit: Button.() -> Unit) : this() {
        unit.invoke(this)
    }

    val text = Bindable("Button")
    lateinit var textDrawable: TextDrawable

    override fun onLoad(dependencyContainer: DependencyContainer) {

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

        text.valueChanged { event ->
            textDrawable.text = event.newValue
        }
    }

    override fun onKeyTyped(event: KeyEvent): Boolean {
        this.animateScale(Vector2f(2f, 2f), 5000, Easing.OUT_ELASTIC)
        return true
    }

}