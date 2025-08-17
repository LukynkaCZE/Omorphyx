package cz.lukynka.omorphyx.renderer.graphics

import cz.lukynka.omorphyx.renderer.graphics.container.Container
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import org.jetbrains.skia.Canvas

abstract class Drawable {

    var x: Float = 0f
    var y: Float = 0f

    var height: Int = 0
    var width: Int = 0

    var anchor: Anchor = Anchor.TOP_LEFT
    var origin: Anchor = Anchor.TOP_LEFT
    var autoSizeAxes: Axes = Axes.NONE

    var parent: Container? = null

    fun parentSize(): Pair<Int, Int> {
        return if (parent != null) Pair(parent!!.width, parent!!.height) else Pair(0, 0)
    }

    open fun updateAutoSizeAxis() {}

    fun alignedPosition(): Pair<Float, Float> {
        val (parentWidth, parentHeight) = parentSize()
        val anchorOffset = anchor.getOffset(parentWidth, parentHeight)
        val originOffset = origin.getOffset(width, height)

        return Pair(anchorOffset.first - originOffset.first + x, anchorOffset.second - originOffset.second + y)
    }

    abstract fun draw(canvas: Canvas)

}