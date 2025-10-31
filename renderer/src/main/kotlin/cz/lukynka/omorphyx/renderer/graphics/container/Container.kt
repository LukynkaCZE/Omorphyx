package cz.lukynka.omorphyx.renderer.graphics.container

class Container() : CompositeDrawable() {

    constructor(unit: Container.() -> Unit) : this() {
        unit.invoke(this)
    }

}