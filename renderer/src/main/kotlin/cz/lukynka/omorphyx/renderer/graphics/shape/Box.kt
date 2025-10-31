package cz.lukynka.omorphyx.renderer.graphics.shape

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.RRect

class Box() : ColorableDrawable() {

    var cornerRadius: Float = 0f

    constructor(unit: Box.() -> Unit) : this() {
        unit.invoke(this)
    }

    override fun onDraw(canvas: Canvas) {
        if (width <= 0 || height <= 0) return

        val roundRect = RRect.makeXYWH(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius)
        canvas.drawRRect(roundRect, getPaint())
    }

}