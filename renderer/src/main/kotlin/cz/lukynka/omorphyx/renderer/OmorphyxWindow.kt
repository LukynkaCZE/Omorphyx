package cz.lukynka.omorphyx.renderer

import cz.lukynka.omorphyx.renderer.graphics.container.CompositeDrawable
import cz.lukynka.omorphyx.renderer.graphics.layout.Axes
import cz.lukynka.omorphyx.renderer.input.KeyCombination
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaLayerRenderDelegate
import org.jetbrains.skiko.SkikoRenderDelegate
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class OmorphyxWindow(val projectName: String) : CompositeDrawable() {

    companion object {
        val KEY_COMBINATION_DEBUG_RENDER = KeyCombination(KeyEvent.VK_F1, KeyEvent.VK_CONTROL)
    }

    private val pressedKeys = mutableSetOf<Int>()

    init {
        this.autoSizeAxes = Axes.NONE
        this.registerKeyCombination(KEY_COMBINATION_DEBUG_RENDER)

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

            layer.addKeyListener(object : KeyListener {

                override fun keyTyped(e: KeyEvent) {
                    this@OmorphyxWindow.onKeyTyped(e)
                }

                override fun keyPressed(e: KeyEvent) {
                    this@OmorphyxWindow.onKeyDown(e)
                }

                override fun keyReleased(e: KeyEvent) {
                    this@OmorphyxWindow.onKeyUp(e)
                }
            })
        }
    }

    override fun onKeyCombination(keyCombination: KeyCombination): Boolean {
        if (keyCombination == KEY_COMBINATION_DEBUG_RENDER) {
            OmorphyxDebug.debugViewer = !OmorphyxDebug.debugViewer
            return true
        }
        return false
    }

    override fun onKeyDown(event: KeyEvent): Boolean {
        pressedKeys.add(event.keyCode)

        val modifiers = pressedKeys.intersect(setOf(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_META))
        val mainKey = event.keyCode

        if (mainKey !in modifiers) {
            val combination = KeyCombination(mainKey, modifiers)
            if (dispatchKeyCombination(combination)) {
                return true
            }
        }

        return super.onKeyDown(event)
    }

    override fun onKeyUp(event: KeyEvent): Boolean {
        pressedKeys.remove(event.keyCode)
        return super.onKeyUp(event)
    }

}