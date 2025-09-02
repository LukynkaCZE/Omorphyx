package cz.lukynka.omorphyx.renderer.graphics

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.OmorphyxDebug
import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.input.KeyCombination
import cz.lukynka.prettylog.log
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.Rect
import java.awt.event.KeyEvent

abstract class Drawable {

    var x: Float = 0f
    var y: Float = 0f

    var height: Int = 0
    var width: Int = 0

    var anchor: Anchor = Anchor.TOP_LEFT
    var origin: Anchor = Anchor.TOP_LEFT

    var autoSizeAxes: Axes = Axes.NONE
    var relativeSizeAxes: Axes = Axes.NONE
    var parent: CompositeDrawable? = null

    internal val registeredKeyCombinations = mutableSetOf<KeyCombination>()

    fun parentSize(): Pair<Int, Int> {
        return if (parent != null) Pair(parent!!.width, parent!!.height) else Pair(0, 0)
    }

    open fun updateDrawableLayout() {
        if (relativeSizeAxes != Axes.NONE) {
            val parent = parentSize()
            val (newX, newY) = when (relativeSizeAxes) {
                Axes.NONE -> width to height
                Axes.X -> parent.first to height
                Axes.Y -> width to parentSize().second
                Axes.BOTH -> parentSize()
            }
            width = newX
            height = newY
        }
    }

    fun alignedPosition(): Pair<Float, Float> {
        val (parentWidth, parentHeight) = parentSize()
        val anchorOffset = anchor.getOffset(parentWidth, parentHeight)
        val originOffset = origin.getOffset(width, height)

        return Pair(anchorOffset.first - originOffset.first + x, anchorOffset.second - originOffset.second + y)
    }

    abstract fun draw(canvas: Canvas)

    open fun drawDebug(canvas: Canvas) {
        if (!OmorphyxDebug.debugViewer) return

        val ox = origin.getOffset(width, height)
        val (parentWidth, parentHeight) = parentSize()
        val ax = anchor.getOffset(parentWidth, parentHeight)

        val boundsPaint = Paint().apply {
            color = Color4.RED.toPacketInt()
            mode = PaintMode.STROKE
            strokeWidth = 2.5f
        }
        val originPaint = Paint().apply {
            color = Color4.GREEN.toPacketInt()
            mode = PaintMode.FILL
        }
        val anchorPaint = Paint().apply {
            color = Color4.PURPLE.toPacketInt()
            mode = PaintMode.FILL
        }

        if (width > 0 && height > 0) {
            val bounds = Rect.makeXYWH(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRect(bounds, boundsPaint)
        }

        val originSize = 10f
        val originHalf = originSize / 2f
        val anchorSize = 8f
        val anchorHalf = anchorSize / 2f

        val originCx = ox.first.toFloat()
        val originCy = ox.second.toFloat()
        val originRect = Rect.makeXYWH(originCx - originHalf, originCy - originHalf, originSize, originSize)
        canvas.drawRect(originRect, originPaint)

        val (px, py) = alignedPosition()
        val parentAnchorX = ax.first - px
        val parentAnchorY = ax.second - py
        val anchorRect = Rect.makeXYWH(parentAnchorX - anchorHalf, parentAnchorY - anchorHalf, anchorSize, anchorSize)
        canvas.drawRect(anchorRect, anchorPaint)
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