package com.hyze.tools

enum class Shop(vararg val npcIds: Any, val key: Int) {

    SLAYER(1, 2, 3, "Spria", key = 29),
    GENERAL(4, 5, 6, key = 1);

    companion object {
        val allIds get() = Shop.entries.flatMap { it.npcIds.toList() }.toTypedArray()

        fun get(npcId: Any): Int? = entries.firstOrNull { npcId in it.npcIds }?.key
    }
}

fun main() {
    println("É á è ê")
}