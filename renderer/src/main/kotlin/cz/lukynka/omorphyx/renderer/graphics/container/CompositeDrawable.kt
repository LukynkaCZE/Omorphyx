package cz.lukynka.omorphyx.renderer.graphics.container

import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.Drawable
import cz.lukynka.omorphyx.renderer.input.KeyCombination
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode
import java.awt.event.KeyEvent
import cz.lukynka.omorphyx.renderer.OmorphyxDebug
import org.jetbrains.skia.Rect

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
        _children.forEach(Drawable::updateDrawableLayout)
    }

    override fun onDraw(canvas: Canvas) {
        _children.forEach { child ->
            canvas.save()
            canvas.concat(child.getTransformationMatrix())
            child.draw(canvas)
            canvas.restore()
        }
    }


    override fun drawDebug(canvas: Canvas) {
        if (!OmorphyxDebug.debugViewer) return
    
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

        children.forEach { child ->
            canvas.save()
        
            val scaledWidth = child.width * child.scale.x
            val scaledHeight = child.height * child.scale.y
        
            val (alignedX, alignedY) = child.alignedPosition()
        
            val originOffset = child.origin.getOffset(child.width, child.height)
            val originX = originOffset.first.toFloat()
            val originY = originOffset.second.toFloat()
        
            val scaledTopLeftX = alignedX + originX - (originX * child.scale.x)
            val scaledTopLeftY = alignedY + originY - (originY * child.scale.y)
        
            val bounds = Rect.makeXYWH(scaledTopLeftX, scaledTopLeftY, scaledWidth, scaledHeight)
            canvas.drawRect(bounds, boundsPaint)
        
            val screenOriginX = alignedX + originX
            val screenOriginY = alignedY + originY
        
            val (parentWidth, parentHeight) = child.parentSize()
            val anchorOffset = child.anchor.getOffset(parentWidth, parentHeight)
            val screenAnchorX = anchorOffset.first.toFloat()
            val screenAnchorY = anchorOffset.second.toFloat()
        
            val originSize = 10f
            val originHalf = originSize / 2f
            val anchorSize = 8f
            val anchorHalf = anchorSize / 2f

            val originRect = Rect.makeXYWH(screenOriginX - originHalf, screenOriginY - originHalf, originSize, originSize)
            canvas.drawRect(originRect, originPaint)

            val anchorRect = Rect.makeXYWH(screenAnchorX - anchorHalf, screenAnchorY - anchorHalf, anchorSize, anchorSize)
            canvas.drawRect(anchorRect, anchorPaint)
        
            canvas.restore()
        
            if (child is CompositeDrawable) {
                canvas.save()
                canvas.concat(child.getTransformationMatrix())
                child.drawDebug(canvas)
                canvas.restore()
            }
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