package cz.lukynka.omorphyx.renderer.dependency

import kotlin.reflect.KClass

class DependencyContainer {
    private val cache = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> cache(instance: T, type: KClass<T>) {
        cache[type] = instance
    }

    inline fun <reified T : Any> cache(instance: T) {
        cache(instance, T::class)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T {
        return cache[type] as? T ?: throw IllegalStateException("Dependency with type ${type.simpleName} is not cached")
    }

    inline fun <reified T : Any> get(): T {
        return get(T::class)
    }

}