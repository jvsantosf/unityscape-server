package com.hyze.commands

interface ArgumentKey<T> {

    val key: String

    operator fun invoke(value: T) = this to value
}

data class ArgumentKeyImpl<T>(
    override val key: String,
): ArgumentKey<T>

class ArgumentNotFoundException(argumentKey: ArgumentKey<*>): Exception("Argument ${argumentKey.key} not found")

fun <T> Argument(key: String): ArgumentKey<T> = ArgumentKeyImpl(key)


