package cz.lukynka.omorphyx.renderer.graphics.shape

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.Drawable

abstract class ColorableDrawable : Drawable() {
    var color: Color4 = Color4.WHITE
}