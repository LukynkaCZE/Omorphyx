package cz.lukynka.omorphyx.renderer.graphics.shape

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Rect

class Box() : ColorableDrawable() {

    constructor(unit: Box.() -> Unit) : this() {
        unit.invoke(this)
    }

    override fun draw(canvas: Canvas) {
        if (width <= 0 || height <= 0) return

        val rect = Rect.makeXYWH(x, y, width.toFloat(), height.toFloat())
        canvas.drawRect(rect, color.toPaint())
    }

}