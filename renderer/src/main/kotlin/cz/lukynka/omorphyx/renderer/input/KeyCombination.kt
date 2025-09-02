package cz.lukynka.omorphyx.renderer.input

data class KeyCombination(val keyCode: Int, val modifiers: Set<Int> = emptySet()) {
    constructor(keyCode: Int, vararg modifiers: Int) : this(keyCode, modifiers.toSet())
}
