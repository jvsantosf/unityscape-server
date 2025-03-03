package com.hyze.plugins

import com.hyze.Engine
import com.hyze.event.Event
import com.hyze.event.EventBus
import com.hyze.event.Listener
import com.hyze.plugins.event.CommandPlugin
import com.hyze.plugins.event.InterfacePlugin
import com.hyze.plugins.event.NPCPlugin
import com.hyze.plugins.event.ObjectPlugin
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
@FunctionDSLMarker
inline fun <reified T : Plugin> on(
    identifiers: Array<*>,
    options: Array<String> = emptyArray(),
    predicates: Array<PluginPredicate<T>> = emptyArray(),
    noinline block: T.() -> Unit
) {
    val manager = when (T::class) {
        ObjectPlugin::class -> objectManager
        NPCPlugin::class -> npcManager
        InterfacePlugin::class -> interfaceManager
        CommandPlugin::class -> commandManager
        else -> throw IllegalArgumentException("Unsupported plugin type: ${T::class}")
    }
    (manager as? PluginEventsManager<T>)?.register(
        T::class,
        identifiers,
        predicates,
        options,
        block
    ) ?: throw IllegalStateException("Manager for ${T::class} is not properly cast.")
}

@FunctionDSLMarker
inline fun <reified T : Event> event(eventBus: EventBus = Engine.eventBus, noinline block: T.() -> Unit) = eventBus
    .registerListener(T::class, object : Listener<T> {
        override fun handle(event: T) {
            block.invoke(event)
        }
    })