package cz.lukynka.omorphyx.renderer.graphics.shape

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.Drawable
import org.jetbrains.skia.BlendMode
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.Shader

abstract class ColorableDrawable : Drawable() {
    var color: Color4 = Color4.WHITE
    var blendMode: BlendMode = BlendMode.SRC_OVER
    var paintMode: PaintMode = PaintMode.FILL
    var shader: Shader? = null

    fun getPaint(): Paint {
        return color.toPaint().apply {
            this.mode = paintMode
            this.blendMode = this@ColorableDrawable.blendMode
            if (shader != null) this.shader = this@ColorableDrawable.shader
        }
    }
}