package cz.lukynka.omorphyx.renderer.graphics.shape

import cz.lukynka.omorphyx.renderer.graphics.Drawable
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color4f
import org.jetbrains.skia.Paint

data class Circle(val radius: Float, val color4f: Color4f) : Drawable() {

    override fun draw(canvas: Canvas) {
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, Paint().apply { color4f = this@Circle.color4f })
    }

}