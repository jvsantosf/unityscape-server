package com.hyze.commands

import com.hyze.plugins.FunctionDSLMarker

class ArgumentsContext(
    val arguments: MutableMap<Int, ArgumentKey<*>> = mutableMapOf()
) {
    private var index: Int = 0

    fun addArgument(key: ArgumentKey<*>) {
        arguments[index++] = key
    }
}

class Command(
    var argumentsContext: ArgumentsContext,
) {

    private var index: Int = 0

    fun arguments(block: ArgumentsContext.() -> Unit = {}) {
        this.argumentsContext = ArgumentsContext()
        this.argumentsContext.block()
    }
}

@FunctionDSLMarker
fun performCommand(command: Command.() -> Unit) {

}

fun test() {
    val yellMessage = Argument<String>("yell")

    performCommand {
        arguments {
            addArgument(yellMessage)
        }
    }
}