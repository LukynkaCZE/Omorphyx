package cz.lukynka.omorphyx.renderer.graphics

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.OmorphyxDebug
import cz.lukynka.omorphyx.renderer.graphics.container.Container
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import org.jetbrains.skia.*

abstract class Drawable {

    var x: Float = 0f
    var y: Float = 0f

    var height: Int = 0
    var width: Int = 0

    var anchor: Anchor = Anchor.TOP_LEFT
    var origin: Anchor = Anchor.TOP_LEFT
    var autoSizeAxes: Axes = Axes.NONE

    var parent: Container? = null

    fun parentSize(): Pair<Int, Int> {
        return if (parent != null) Pair(parent!!.width, parent!!.height) else Pair(0, 0)
    }

    open fun updateAutoSizeAxis() {}

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


}