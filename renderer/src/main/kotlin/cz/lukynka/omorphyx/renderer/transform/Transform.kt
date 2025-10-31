package cz.lukynka.omorphyx.renderer.transform

import cz.lukynka.omorphyx.renderer.graphics.layout.vector.Vector2f

sealed class Transform<T> {

    companion object {
        val LINEAR_FLOAT = FloatTransform()
        val LINEAR_VECTOR2F = Vector2fTransform()
        val LINEAR_INT = IntTransform()
    }

    abstract fun apply(startValue: T, endValue: T, progress: Float): T

    data class FloatTransform(val transform: (Float, Float, Float) -> Float = { start, end, progress ->
        start + (end - start) * progress
    }) : Transform<Float>() {
        override fun apply(startValue: Float, endValue: Float, progress: Float): Float {
            return transform(startValue, endValue, progress)
        }
    }

    data class Vector2fTransform(val transform: (Vector2f, Vector2f, Float) -> Vector2f = { start, end, progress ->
        Vector2f(
            start.x + (end.x - start.x) * progress,
            start.y + (end.y - start.y) * progress
        )
    }) : Transform<Vector2f>() {
        override fun apply(startValue: Vector2f, endValue: Vector2f, progress: Float): Vector2f {
            return transform(startValue, endValue, progress)
        }
    }

    data class IntTransform(val transform: (Int, Int, Float) -> Int = { start, end, progress ->
        (start + (end - start) * progress).toInt()
    }) : Transform<Int>() {
        override fun apply(startValue: Int, endValue: Int, progress: Float): Int {
            return transform(startValue, endValue, progress)
        }
    }
}