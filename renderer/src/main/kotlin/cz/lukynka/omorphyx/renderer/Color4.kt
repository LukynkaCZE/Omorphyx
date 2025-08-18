package cz.lukynka.omorphyx.renderer

import org.jetbrains.skia.Color
import org.jetbrains.skia.Color4f
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode
import kotlin.math.roundToInt

data class Color4(val r: Int, val g: Int, val b: Int, val a: Int) {

    companion object {
        val WHITE = Color4(255, 255, 255, 255)
        val BLACK = Color4(0, 0, 0, 255)
        val TRANSPARENT = Color4(0, 0, 0, 0)

        val RED = Color4(255, 0, 0, 255)
        val GREEN = Color4(0, 255, 0, 255)
        val BLUE = Color4(0, 0, 255, 255)

        val YELLOW = Color4(255, 255, 0, 255)
        val CYAN = Color4(0, 255, 255, 255)
        val MAGENTA = Color4(255, 0, 255, 255)

        val GRAY = Color4(128, 128, 128, 255)
        val DARK_GRAY = Color4(64, 64, 64, 255)
        val LIGHT_GRAY = Color4(192, 192, 192, 255)

        val ORANGE = Color4(255, 165, 0, 255)
        val PURPLE = Color4(140, 71, 237, 255)
        val PINK = Color4(255, 192, 203, 255)
        val TEAL = Color4(0, 128, 128, 255)

        fun fromFloats(r: Float, g: Float, b: Float, a: Float = 1f): Color4 {
            fun c(x: Float) = (x.coerceIn(0f, 1f) * 255f).roundToInt()
            return Color4(c(r), c(g), c(b), c(a))
        }

        fun fromPacketInt(argb: Int): Color4 {
            val a = (argb ushr 24) and 0xFF
            val r = (argb ushr 16) and 0xFF
            val g = (argb ushr 8) and 0xFF
            val b = (argb) and 0xFF
            return Color4(r, g, b, a)
        }
    }

    fun toPacketInt(): Int {
        return Color.makeARGB(a, r, g, b)
    }

    fun toColor4f(): Color4f = Color4f(r / 255f, g / 255f, b / 255f, a / 255f)

    fun withAlpha(alpha: Int): Color4 = copy(a = alpha.coerceIn(0, 255))

    fun toPaint(mode: PaintMode = PaintMode.FILL): Paint {
        return Paint().apply {
            this.color4f = this@Color4.toColor4f()
            this.mode = mode
        }
    }
}