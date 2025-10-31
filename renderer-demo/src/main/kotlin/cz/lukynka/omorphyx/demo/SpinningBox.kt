package cz.lukynka.omorphyx.demo

import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.container.FillFlowContainer
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.shape.Box
import cz.lukynka.omorphyx.renderer.graphics.shape.Circle
import org.jetbrains.skia.Canvas

class SpinningBox : CompositeDrawable() {

    var box: FillFlowContainer = addChild(FillFlowContainer {
        width = 100
        height = 100
        anchor = Anchor.CENTER
        origin = Anchor.CENTER
        direction.value = FillFlowContainer.Direction.VERTICAL
        spacing.value = 10

        addChild(Box {
            relativeSizeAxes = Axes.BOTH
            anchor = Anchor.CENTER
            origin = Anchor.CENTER
        })
        addChild(Circle {
            anchor = Anchor.CENTER
            origin = Anchor.CENTER
            relativeSizeAxes = Axes.BOTH
        })
    })

    override fun onDraw(canvas: Canvas) {
        this.rotation += 1f
        super.onDraw(canvas)
    }
}