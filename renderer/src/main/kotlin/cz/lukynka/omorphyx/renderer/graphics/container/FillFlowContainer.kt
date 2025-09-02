package cz.lukynka.omorphyx.renderer.graphics.container

import cz.lukynka.Bindable
import cz.lukynka.omorphyx.renderer.Color4
import cz.lukynka.omorphyx.renderer.graphics.Drawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.Rect

class FillFlowContainer() : CompositeDrawable() {

    constructor(unit: FillFlowContainer.() -> Unit) : this() {
        unit.invoke(this)
    }

    enum class Direction {
        HORIZONTAL,
        VERTICAL
    }

    override fun <T : Drawable> addChild(child: T): T {
        super.addChild(child)
        invalidateLayout()
        return child
    }

    val direction: Bindable<Direction> = Bindable(Direction.VERTICAL)
    val spacing: Bindable<Int> = Bindable(0)

    init {
        direction.valueChanged { invalidateLayout() }
        spacing.valueChanged { invalidateLayout() }
    }

    fun invalidateLayout() {
        layoutInvalid = true
    }

    private var layoutInvalid = true

    override fun draw(canvas: Canvas) {
        updateDrawableLayout()
        var currentOffset = 0f

        children.forEach { child ->
            canvas.save()
            when (direction.value) {
                Direction.HORIZONTAL -> {
                    canvas.translate(currentOffset, 0f)
                    currentOffset += child.width + spacing.value
                }

                Direction.VERTICAL -> {
                    canvas.translate(0f, currentOffset)
                    currentOffset += child.height + spacing.value
                }
            }
            val (x, y) = child.alignedPosition()
            canvas.translate(x, y)

            child.draw(canvas)
            canvas.restore()
        }
    }

    override fun drawDebug(canvas: Canvas) {
        updateDrawableLayout()
        var currentOffset = 0f

        children.forEach { child ->
            canvas.save()
            when (direction.value) {
                Direction.HORIZONTAL -> {
                    canvas.translate(currentOffset, 0f)
                    currentOffset += child.width + spacing.value
                }

                Direction.VERTICAL -> {
                    canvas.translate(0f, currentOffset)
                    currentOffset += child.height + spacing.value
                }
            }

            val (x, y) = child.alignedPosition()
            canvas.translate(x, y)

            child.drawDebug(canvas)
            canvas.restore()
        }

        if (width > 0 && height > 0) {
            val bounds = Rect.makeXYWH(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRect(bounds, Color4.YELLOW.toPaint(PaintMode.STROKE))
        }
    }

    override fun updateDrawableLayout() {
        super.updateDrawableLayout()
        if (autoSizeAxes != Axes.NONE) {

            if (layoutInvalid) {
                layoutInvalid = false
            }

            var totalWidth = 0
            var totalHeight = 0
            var currentOffset = 0

            children.forEach { child ->
                child.updateDrawableLayout()

                when (direction.value) {
                    Direction.HORIZONTAL -> {
                        totalWidth = maxOf(totalWidth, currentOffset + child.width) // Track the maximum extent
                        totalHeight = maxOf(totalHeight, child.height)
                        currentOffset += child.width + spacing.value
                    }

                    Direction.VERTICAL -> {
                        totalWidth = maxOf(totalWidth, child.width)
                        totalHeight = maxOf(totalHeight, currentOffset + child.height) // Track the maximum extent
                        currentOffset += child.height + spacing.value
                    }
                }
            }

            when (autoSizeAxes) {
                Axes.X -> width = totalWidth
                Axes.Y -> height = totalHeight
                Axes.BOTH -> {
                    width = totalWidth
                    height = totalHeight
                }

                else -> {}
            }
        }
    }
}