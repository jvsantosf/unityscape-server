package com.hyze.utils

class IndexedQueue<T> {
    private val map = mutableMapOf<Int, T>()
    private var currentIndex = 0

    fun offer(element: T) {
        map[map.size] = element
    }

    fun poll(): T? {
        val element = map[currentIndex]
        if (element != null) {
            currentIndex++
        }
        return element
    }

    fun peek(): T? {
        return map[currentIndex]
    }

    fun size(): Int {
        return map.size
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    fun clear() {
        map.clear()
        currentIndex = 0
    }

    fun getCurrentIndex(): Int {
        return currentIndex
    }

    fun all(): List<T> {
        return map.values.toList()
    }
}