package cz.lukynka.omorphyx.renderer.graphics.container

import cz.lukynka.omorphyx.renderer.graphics.Drawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.input.KeyCombination
import org.jetbrains.skia.Canvas
import java.awt.event.KeyEvent

open class CompositeDrawable : Drawable() {

    private val _children = mutableListOf<Drawable>()
    val children get() = _children.toList()

    open fun <T : Drawable> addChild(child: T): T {
        child.parent = this
        _children.add(child)
        return child
    }

    override fun updateDrawableLayout() {
        super.updateDrawableLayout()
        children.forEach { child ->
            child.updateDrawableLayout()
        }
        if (autoSizeAxes != Axes.NONE) {
            var maxWidth = 0
            var maxHeight = 0

            for (child in children) {
                child.updateDrawableLayout()
                val (childX, childY) = child.alignedPosition()
                val childRight = childX.toInt() + child.width
                val childBottom = childY.toInt() + child.height
                if (childRight > maxWidth) maxWidth = childRight
                if (childBottom > maxHeight) maxHeight = childBottom
            }

            when (autoSizeAxes) {
                Axes.X -> width = maxWidth
                Axes.Y -> height = maxHeight
                Axes.BOTH -> {
                    width = maxWidth
                    height = maxHeight
                }

                else -> {}
            }
        }
    }

    override fun draw(canvas: Canvas) {
        updateDrawableLayout()
        _children.forEach { child ->
            val (x, y) = child.alignedPosition()
            canvas.save()
            canvas.translate(x, y)
            child.draw(canvas)
            canvas.restore()
        }
    }

    override fun drawDebug(canvas: Canvas) {
        children.forEach { child ->
            val (x, y) = child.alignedPosition()
            canvas.save()
            canvas.translate(x, y)
            child.drawDebug(canvas)
            canvas.restore()
        }
        super.drawDebug(canvas)
    }

    override fun dispatchKeyCombination(combination: KeyCombination): Boolean {
        for (child in children.asReversed()) {
            if (child.dispatchKeyCombination(combination)) {
                return true
            }
        }

        return super.dispatchKeyCombination(combination)
    }

    override fun onKeyDown(event: KeyEvent): Boolean {
        for (child in children.asReversed()) {
            if (child.onKeyDown(event)) {
                return true
            }
        }
        return super.onKeyDown(event)
    }


    override fun onKeyUp(event: KeyEvent): Boolean {
        for (child in children.reversed()) {
            if (child.onKeyUp(event)) {
                return true
            }
        }
        return super.onKeyUp(event)
    }

    override fun onKeyTyped(event: KeyEvent): Boolean {
        for (child in children.reversed()) {
            if (child.onKeyTyped(event)) {
                return true
            }
        }
        return super.onKeyTyped(event)
    }
}