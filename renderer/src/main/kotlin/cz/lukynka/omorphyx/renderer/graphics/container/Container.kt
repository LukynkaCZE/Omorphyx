package cz.lukynka.omorphyx.renderer.graphics.container

import cz.lukynka.omorphyx.renderer.graphics.Drawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import org.jetbrains.skia.Canvas

open class Container : Drawable() {
    private val _children = mutableListOf<Drawable>()
    val children get() = _children.toList()

    fun add(child: Drawable) {
        child.parent = this
        _children.add(child)
    }

    override fun updateAutoSizeAxis() {
        if (autoSizeAxes == Axes.NONE) return
        var maxWidth = 0
        var maxHeight = 0

        for (child in children) {
            child.updateAutoSizeAxis()
            val (childX, childY) = child.alignedPosition()
            val childRight = childX.toInt() + child.width
            val childBottom = childY.toInt() + child.height
            if (childRight > maxWidth) maxWidth = childRight
            if (childBottom > maxHeight) maxHeight = childBottom
        }

        when (autoSizeAxes) {
            Axes.X -> width = maxWidth
            Axes.Y -> height = maxHeight
            Axes.BOTH -> {
                width = maxWidth
                height = maxHeight
            }

            else -> {}
        }
    }

    override fun draw(canvas: Canvas) {
        updateAutoSizeAxis()
        _children.forEach { child ->
            val (x, y) = child.alignedPosition()
            canvas.save()
            canvas.translate(x, y)
            child.draw(canvas)
            canvas.restore()
        }
    }
}