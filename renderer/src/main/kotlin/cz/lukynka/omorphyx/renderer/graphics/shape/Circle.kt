package cz.lukynka.omorphyx.renderer.graphics.shape

import org.jetbrains.skia.Canvas

class Circle() : ColorableDrawable() {

    constructor(unit: Circle.() -> Unit) : this() {
        unit.invoke(this)
    }

    override fun onDraw(canvas: Canvas) {
        val radius = (minOf(width, height) / 2).toFloat()
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, getPaint())
    }

}
