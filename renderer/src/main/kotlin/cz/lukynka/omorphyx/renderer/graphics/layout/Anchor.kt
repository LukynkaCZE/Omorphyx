package cz.lukynka.omorphyx.renderer.graphics.layout

enum class Anchor {
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    CENTER_LEFT,
    CENTER,
    CENTER_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT;

    fun getOffset(parentWidth: Int, parentHeight: Int): Pair<Int, Int> {
        return when (this) {
            TOP_LEFT -> Pair(0, 0)
            TOP_CENTER -> Pair(parentWidth / 2, 0)
            TOP_RIGHT -> Pair(parentWidth, 0)
            CENTER_LEFT -> Pair(0, parentHeight / 2)
            CENTER -> Pair(parentWidth / 2, parentHeight / 2)
            CENTER_RIGHT -> Pair(parentWidth, parentHeight / 2)
            BOTTOM_LEFT -> Pair(0, parentHeight)
            BOTTOM_CENTER -> Pair(parentWidth / 2, parentHeight)
            BOTTOM_RIGHT -> Pair(parentWidth, parentHeight)
        }
    }
}