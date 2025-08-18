package cz.lukynka.omorphyx.renderer

import cz.lukynka.omorphyx.renderer.graphics.container.Container
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaLayerRenderDelegate
import org.jetbrains.skiko.SkikoRenderDelegate
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class OmorphyxWindow(val projectName: String) : Container() {

    init {
        this.autoSizeAxes = Axes.NONE

        val layer = SkiaLayer()
        layer.renderDelegate = SkiaLayerRenderDelegate(layer, object : SkikoRenderDelegate {

            override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
                canvas.clear(Color.BLACK)
                this@OmorphyxWindow.width = width
                this@OmorphyxWindow.height = height
                this@OmorphyxWindow.draw(canvas)
                if (OmorphyxDebug.debugViewer) this@OmorphyxWindow.drawDebug(canvas)
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