package cz.lukynka.omorphyx.demo

import cz.lukynka.omorphyx.renderer.OmorphyxDebug
import cz.lukynka.omorphyx.renderer.OmorphyxWindow
import cz.lukynka.omorphyx.renderer.graphics.container.FillFlowContainer
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.shape.Box
import cz.lukynka.omorphyx.renderer.graphics.shape.Circle
import kotlin.random.Random

val fillFlow = FillFlowContainer {
    direction.value = FillFlowContainer.Direction.HORIZONTAL
    anchor = Anchor.CENTER
    origin = Anchor.CENTER
    autoSizeAxes = Axes.BOTH
    spacing.value = 20
}

fun main() {
    OmorphyxDebug.debugViewer = true

    val window = OmorphyxWindow("Demo")
    window.add(fillFlow)

    repeat(5) { _ ->
        val box = Box {
            width = 50
            anchor = Anchor.BOTTOM_LEFT
            origin = Anchor.BOTTOM_LEFT
            height = Random.nextInt(50, 100)
        }
        fillFlow.add(box)
    }
    val circle = Circle {
        width = 50
        height = 50
        anchor = Anchor.BOTTOM_LEFT
        origin = Anchor.BOTTOM_LEFT
    }
    fillFlow.add(circle)

    fillFlow.invalidateLayout()
}