package cz.lukynka.omorphyx.renderer.graphics.debug

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.container.FillFlowContainer
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.shape.Box

class DebugPopup() : CompositeDrawable() {

    var title: String = "Title"
    var text: String = "Text"

    constructor(unit: DebugPopup.() -> Unit) : this() {
        unit.invoke(this)
    }

    var background: Box
    var fillFlowContainer: FillFlowContainer
    lateinit var titleDrawable: TextDrawable
    lateinit var textDrawable: TextDrawable

    init {
        this.height = 100
        this.width = 300

        background = this.addChild(Box {
            color = Color4.PURPLE
            relativeSizeAxes = Axes.BOTH
            cornerRadius = 35f
        })

        fillFlowContainer = this.addChild(FillFlowContainer {

            relativeSizeAxes = Axes.BOTH
            anchor = Anchor.CENTER
            origin = Anchor.CENTER
            direction.value = FillFlowContainer.Direction.VERTICAL
            spacing.value = 15

            titleDrawable = this.addChild(TextDrawable {
                origin = Anchor.CENTER
                anchor = Anchor.CENTER
                text = this@DebugPopup.title
                fontSize = 24f
            })

            textDrawable = this.addChild(TextDrawable {
                origin = Anchor.CENTER
                anchor = Anchor.CENTER
                text = this@DebugPopup.text
                fontSize = 16f
            })
        })
    }
}