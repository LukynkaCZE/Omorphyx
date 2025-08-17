package cz.lukynka.omorphyx.renderer

import cz.lukynka.omorphyx.renderer.graphics.container.Container
import cz.lukynka.omorphyx.renderer.graphics.layout.Anchor
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.graphics.shape.Circle
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Color4f
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaLayerRenderDelegate
import org.jetbrains.skiko.SkikoRenderDelegate
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class OmorphyxWindow(val projectName: String) {

    private val root = Container().apply {
        autoSizeAxes = Axes.NONE
    }

    init {
        val circle = Circle(50f, Color4f(Color.RED)).apply {
            anchor = Anchor.CENTER
            origin = Anchor.CENTER
            x = 0f
            y = 0f
            width = 100
            height = 100
        }

        root.add(circle)

        val layer = SkiaLayer()
        layer.renderDelegate = SkiaLayerRenderDelegate(layer, object : SkikoRenderDelegate {

            override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
                root.width = width
                root.height = height
                root.draw(canvas)
            }
        })

        SwingUtilities.invokeLater {
            val window = JFrame("Omorphyx running \"$projectName\"").apply {
                defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            }
            layer.attachTo(window.contentPane)
            layer.needRedraw()
            window.pack()
            window.isVisible = true
        }
    }

}