package cz.lukynka.omorphyx.renderer.graphics.container

import cz.lukynka.bindables.Bindable
import cz.lukynka.omorphyx.renderer.graphics.Drawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import org.jetbrains.skia.Canvas

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

    private var contentWidth = 0
    private var contentHeight = 0

    init {
        direction.valueChanged { invalidateLayout() }
        spacing.valueChanged { invalidateLayout() }
    }

    fun invalidateLayout() {
        layoutInvalid = true
    }

    private var layoutInvalid = true

    override fun onDraw(canvas: Canvas) {
        updateDrawableLayout()

        var currentOffset: Float = when (direction.value) {
            Direction.HORIZONTAL -> (width - contentWidth) / 2f
            Direction.VERTICAL -> (height - contentHeight) / 2f
        }

        children.forEach { child ->
            canvas.save()
            val (x, y) = child.alignedPosition()
            when (direction.value) {
                Direction.HORIZONTAL -> {
                    canvas.translate(currentOffset, y)
                    currentOffset += child.width + spacing.value
                }

                Direction.VERTICAL -> {
                    canvas.translate(x, currentOffset)
                    currentOffset += child.height + spacing.value
                }
            }

            child.onDraw(canvas)
            canvas.restore()
        }
    }

    override fun updateDrawableLayout() {
        super.updateDrawableLayout()

        if (layoutInvalid) {
            layoutInvalid = false

            children.forEach { it.updateDrawableLayout() }

            var calculatedWidth = 0
            var calculatedHeight = 0

            if (children.isNotEmpty()) {
                when (direction.value) {
                    Direction.HORIZONTAL -> {
                        calculatedWidth = children.sumOf { it.width } + (children.size - 1).coerceAtLeast(0) * spacing.value
                        calculatedHeight = children.maxOfOrNull { it.height } ?: 0
                    }

                    Direction.VERTICAL -> {
                        calculatedWidth = children.maxOfOrNull { it.width } ?: 0
                        calculatedHeight = children.sumOf { it.height } + (children.size - 1).coerceAtLeast(0) * spacing.value
                    }
                }
            }

            contentWidth = calculatedWidth
            contentHeight = calculatedHeight
        }

        if (autoSizeAxes != Axes.NONE) {
            when (autoSizeAxes) {
                Axes.X -> width = contentWidth
                Axes.Y -> height = contentHeight
                Axes.BOTH -> {
                    width = contentWidth
                    height = contentHeight
                }

                else -> {}
            }
        }
    }
}