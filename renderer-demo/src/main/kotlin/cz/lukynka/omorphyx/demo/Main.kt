package cz.lukynka.omorphyx.demo

import cz.lukynka.omorphyx.renderer.OmorphyxWindow
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor

fun main() {
    val window = OmorphyxWindow("Demo")

    window.addChild(Button {
        anchor = Anchor.CENTER
        origin = Anchor.CENTER
        height = 60
        width = 220
    })

}