package com.hyze.event

import com.hyze.utils.Logger
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.KClass


class EventBus {

    private val listeners: HashMap<KClass<out Event>, CopyOnWriteArrayList<Listener<Event>>> = HashMap()

    fun <T : Event> registerListener(eventClass: KClass<out T>, listener: Listener<T>): Listener<T> {
        listeners.computeIfAbsent(eventClass) { CopyOnWriteArrayList() }
            .add(listener)

        return listener
    }

    fun <T : Event> callEvent(event: T) {
        val listeners: CopyOnWriteArrayList<Listener<Event>> = listeners[event::class] ?: return Logger.error("Listener '${event::class.java.simpleName}' was not registered.'")
        listeners.forEach {
            it.handle(event)
        }
    }

    fun <T : Event> removeListener(event: KClass<out T>, listener: Listener<T>) {
        listeners[event]?.remove(listener)
    }

    fun clearListeners() {
        listeners.clear()
    }
}