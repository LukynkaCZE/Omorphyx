package cz.lukynka.omorphyx.renderer.transform

class Animation<T>(
    private val startValue: T,
    private val endValue: T,
    private val duration: Long,
    private val easing: Easing = Easing.IN_SINE,
    private val transform: Transform<T>,
    private val onUpdate: (T) -> Unit,
    private val onComplete: (() -> Unit)? = null,
    private val delay: Long = 0L
) {
    private var startTime: Long = -1
    private var isCompleted = false
    private var isPaused = false
    private var pausedTime: Long = 0

    val isRunning: Boolean
        get() = startTime != -1L && !isCompleted && !isPaused

    val isFinished: Boolean
        get() = isCompleted

    fun start(currentTime: Long) {
        if (startTime == -1L) {
            startTime = currentTime + delay
        }
    }

    fun pause() {
        if (isRunning) {
            isPaused = true
        }
    }

    fun resume(currentTime: Long) {
        if (isPaused) {
            val elapsedBeforePause = pausedTime
            startTime = currentTime - elapsedBeforePause
            isPaused = false
        }
    }

    fun stop() {
        isCompleted = true
    }

    fun reset() {
        startTime = -1L
        isCompleted = false
        isPaused = false
        pausedTime = 0
    }

    fun update(currentTime: Long): Boolean {
        if (startTime == -1L || isCompleted || isPaused) return false

        if (currentTime < startTime) return false // Still in delay phase

        val elapsed = currentTime - startTime
        if (isPaused) {
            pausedTime = elapsed
            return false
        }

        if (elapsed >= duration) {
            // Animation finished
            onUpdate.invoke(endValue)
            isCompleted = true
            onComplete?.invoke()
            return true
        }

        val progress = elapsed.toFloat() / duration.toFloat()
        val easedProgress = easing.timeSupplier.invoke(progress)
        val currentValue = transform.apply(startValue, endValue, easedProgress)

        onUpdate.invoke(currentValue)
        return false
    }
}
