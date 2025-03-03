package com.hyze.plugins

data class PluginPredicate<T : Plugin>(
    var failure: T.() -> Unit = {},
    var predicate: T.() -> Boolean = { true },
) {

    fun onFailure(block: T.() -> Unit) {
        failure = block
    }

    fun successIf(block: T.() -> Boolean) {
        predicate = block
    }
}

@FunctionDSLMarker
fun <T : Plugin> predicate(block: PluginPredicate<T>.() -> Unit): PluginPredicate<T> {
    val predicate = PluginPredicate<T>()
    predicate.block()
    return predicate
}