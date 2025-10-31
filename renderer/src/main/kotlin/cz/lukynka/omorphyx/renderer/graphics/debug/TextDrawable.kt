package cz.lukynka.omorphyx.renderer.graphics.debug

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.Drawable
import org.jetbrains.skia.*

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

    private val font = Font().apply {
        size = fontSize
    }
    private var textLine: TextLine? = null

    constructor(unit: TextDrawable.() -> Unit) : this() {
        unit.invoke(this)
    }

    private fun updateTextBounds() {
        if (text.isNotEmpty()) {
            textLine = TextLine.make(text, font)
            width = textLine!!.width.toInt()
            height = (textLine!!.height / 2f).toInt()
        } else {
            width = 0
            height = 0
        }
    }

    override fun onDraw(canvas: Canvas) {

        val paint = color.toPaint().apply {
            isAntiAlias = true
            mode = PaintMode.FILL
        }

        textLine?.let {
            val yOffset = (y + ((height) - font.metrics.ascent))
            canvas.drawTextLine(it, x, yOffset, paint)
        }
    }
}
