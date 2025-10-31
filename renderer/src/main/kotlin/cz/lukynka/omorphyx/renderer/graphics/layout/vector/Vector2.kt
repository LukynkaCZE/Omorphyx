package cz.lukynka.omorphyx.renderer.graphics.layout.vector

data class Vector2(val x: Int, val y: Int) {
    companion object {
        val ZERO = Vector2(0, 0)
    }
}

data class Vector2f(val x: Float, val y: Float) {
    constructor(scale: Float) : this(scale, scale)
    constructor() : this(1f)
}