package cz.lukynka.omorphyx.renderer.transform

import cz.lukynka.kairos.Scheduler
import kotlin.time.Duration.Companion.milliseconds

class AnimationManager(scheduler: Scheduler) {
    private val animations = mutableListOf<Animation<*>>()
    private val sequences = mutableListOf<AnimationSequence>()
    private var currentTime: Long = System.currentTimeMillis()

    init {
        scheduler.runRepeating(1.milliseconds) {
            currentTime = System.currentTimeMillis()

            animations.removeAll { animation ->
                animation.update(currentTime)
                animation.isFinished
            }

            sequences.removeAll { sequence ->
                sequence.update(currentTime)
                sequence.isFinished
            }
        }
    }

    fun <T> addAnimation(animation: Animation<T>) {
        animations.add(animation)
        animation.start(currentTime)
    }

    fun addSequence(sequence: AnimationSequence) {
        sequences.add(sequence)
        sequence.start(currentTime)
    }

    fun clear() {
        animations.clear()
        sequences.clear()
    }

    fun stopAll() {
        animations.forEach { animation -> animation.stop() }
        sequences.forEach { animation -> animation.stop() }
    }

    val activeAnimationCount: Int
        get() = animations.size + sequences.size
}
