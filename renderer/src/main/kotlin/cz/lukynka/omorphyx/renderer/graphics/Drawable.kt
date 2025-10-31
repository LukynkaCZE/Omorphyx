package cz.lukynka.omorphyx.renderer.graphics

import cz.lukynka.omorphyx.renderer.OmorphyxDebug
import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.layout.vector.Vector2
import cz.lukynka.omorphyx.renderer.graphics.layout.vector.Vector2f
import cz.lukynka.omorphyx.renderer.input.KeyCombination
import org.jetbrains.skia.*
import java.awt.event.KeyEvent

abstract class Drawable {

    var x: Float = 0f
    var y: Float = 0f

    var height: Int = 0
    var width: Int = 0

    var scale: Vector2f = Vector2f()
    var rotation: Float = 0f

    var anchor: Anchor = Anchor.TOP_LEFT
    var origin: Anchor = Anchor.TOP_LEFT

    var autoSizeAxes: Axes = Axes.NONE
    var relativeSizeAxes: Axes = Axes.NONE
    var parent: CompositeDrawable? = null

    internal val registeredKeyCombinations = mutableSetOf<KeyCombination>()

    fun parentSize(): Vector2 {
        return if (parent != null) Vector2(parent!!.width, parent!!.height) else Vector2.ZERO
    }

    fun getTransformationMatrix(): Matrix33 {
        val (parentWidth, parentHeight) = parentSize()

        val anchorOffset = anchor.getOffset(parentWidth, parentHeight)
        val originOffset = origin.getOffset(width, height)

        val finalX = anchorOffset.first - originOffset.first + x
        val finalY = anchorOffset.second - originOffset.second + y

        val transform = Matrix33.makeTranslate(
            finalX + originOffset.first.toFloat(),
            finalY + originOffset.second.toFloat()
        )
            .makeConcat(Matrix33.makeRotate(rotation))
            .makeConcat(Matrix33.makeScale(scale.x, scale.y))
            .makeConcat(
                Matrix33.makeTranslate(
                    -originOffset.first.toFloat(),
                    -originOffset.second.toFloat()
                )
            )

        return transform
    }

    open fun updateDrawableLayout() {
        if (relativeSizeAxes != Axes.NONE) {
            val parent = parentSize()
            val (newX, newY) = when (relativeSizeAxes) {
                Axes.NONE -> Vector2(width, height)
                Axes.X -> Vector2(parent.x, height)
                Axes.Y -> Vector2(width, parentSize().y)
                Axes.BOTH -> parentSize()
            }
            width = newX
            height = newY
        }
    }

    fun alignedPosition(): Vector2f {
        val (parentWidth, parentHeight) = parentSize()
        val anchorOffset = anchor.getOffset(parentWidth, parentHeight)
        val originOffset = origin.getOffset(width, height)

        return Vector2f(anchorOffset.first - originOffset.first + x, anchorOffset.second - originOffset.second + y)
    }

    abstract fun onDraw(canvas: Canvas)

    fun draw(canvas: Canvas) {
        updateDrawableLayout()
        onDraw(canvas)
    }

    open fun drawDebug(canvas: Canvas) {
        if (!OmorphyxDebug.debugViewer) return
    }

    open fun onKeyDown(event: KeyEvent): Boolean {
        return false
    }

    open fun onKeyUp(event: KeyEvent): Boolean {
        return false
    }

    open fun onKeyTyped(event: KeyEvent): Boolean {
        return false
    }

    fun registerKeyCombination(vararg keyCombinations: KeyCombination) {
        this.registeredKeyCombinations.addAll(keyCombinations)
    }

    open fun onKeyCombination(keyCombination: KeyCombination): Boolean {
        return false
    }

    internal open fun dispatchKeyCombination(combination: KeyCombination): Boolean {
        if (registeredKeyCombinations.contains(combination)) {
            if (onKeyCombination(combination)) {
                return true
            }
        }
        return false
    }
}