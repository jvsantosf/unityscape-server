package com.hyze.plugins

data class PluginData<T : Plugin>(
    val options: Array<String> = emptyArray(),
    val predicates: Array<PluginPredicate<T>>,
    val block: (T) -> Unit
) {


    override fun hashCode(): Int {
        var result = options.contentHashCode()
        result = 31 * result + predicates.contentHashCode()
        result = 31 * result + block.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PluginData<*>

        if (!options.contentEquals(other.options)) return false
        if (!predicates.contentEquals(other.predicates)) return false
        if (block != other.block) return false

        return true
    }
}