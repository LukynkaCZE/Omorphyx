package cz.lukynka.omorphyx.renderer.graphics.debug

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.Drawable
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine

class TextDrawable() : Drawable() {

    var text: String = ""
        set(value) {
            field = value
            updateTextBounds()
        }

    var fontSize: Float = 16f
        set(value) {
            field = value
            font.size = value
            updateTextBounds()
        }

    var color: Color4 = Color4.WHITE

    private val font = Font().apply { size = fontSize }
    private var textLine: TextLine? = null

    constructor(unit: TextDrawable.() -> Unit) : this() {
        unit.invoke(this)
    }

    private fun updateTextBounds() {
        if (text.isNotEmpty()) {
            textLine = TextLine.make(text, font)
            width = textLine!!.width.toInt()
            height = textLine!!.height.toInt()
        } else {
            width = 0
            height = 0
        }
    }

    override fun draw(canvas: Canvas) {
        textLine?.let {
//            val yOffset = (height - font.metrics.ascent) / 2f
            canvas.drawTextLine(it, 0f, 0f, color.toPaint())
        }
    }
}
