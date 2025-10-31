package cz.lukynka.omorphyx.renderer.transform

class AnimationSequence {
    private val animations = mutableListOf<Animation<*>>()
    private var currentIndex = 0
    private var isRunning = false
    private var onCompleteCallback: (() -> Unit)? = null

    fun <T> then(animation: Animation<T>): AnimationSequence {
        animations.add(animation)
        return this
    }

    fun onComplete(callback: () -> Unit): AnimationSequence {
        onCompleteCallback = callback
        return this
    }

    fun start(currentTime: Long) {
        if (animations.isEmpty()) return

        isRunning = true
        currentIndex = 0
        animations[0].start(currentTime)
    }

    fun update(currentTime: Long) {
        if (!isRunning || currentIndex >= animations.size) return

        val currentAnimation = animations[currentIndex]
        val finished = currentAnimation.update(currentTime)

        if (finished) {
            currentIndex++
            if (currentIndex >= animations.size) {
                isRunning = false
                onCompleteCallback?.invoke()
            } else {
                animations[currentIndex].start(currentTime)
            }
        }
    }

    fun stop() {
        if (isRunning && currentIndex < animations.size) {
            animations[currentIndex].stop()
        }
        isRunning = false
    }

    fun reset() {
        animations.forEach { it.reset() }
        currentIndex = 0
        isRunning = false
    }

    val isFinished: Boolean
        get() = !isRunning && currentIndex >= animations.size
}
