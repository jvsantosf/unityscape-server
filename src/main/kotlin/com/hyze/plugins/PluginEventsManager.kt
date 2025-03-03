package com.hyze.plugins

import kotlin.reflect.KClass

class PluginEventsManager<T : Plugin> {

    private val handlerList = hashMapOf<KClass<out T>, MutableMap<Any, MutableList<PluginData<T>>>>()

    fun dispatch(plugin: T) {
        val data =
            find(plugin) ?: return
        data.block.invoke(plugin)
    }

    private fun find(plugin: T): PluginData<T>? {
        val type = plugin::class

        if (!exists(type, plugin.identifiers)) {
            return null
        }

        val key = handlerList[type]!!.keys.first {
            it in plugin.identifiers
        }
        val pluginsData =
            handlerList[type]!![key] ?: return null
        val foundData = pluginsData.firstOrNull { data ->
            data.predicates.all {
                val pass = it.predicate.invoke(plugin)

                if (!pass) {
                    it.failure.invoke(plugin)
                }

                return@all pass
            }
        }

        if (foundData == null) {
            return null
        }

        if (foundData.options.isNotEmpty()) {
            if (!foundData.options.all { it in plugin.options }) {
                return null
            }
        }

        return foundData
    }

    private fun existsType(type: KClass<out T>): Boolean {
        return handlerList.containsKey(type)
    }

    private fun exists(type: KClass<out T>, keys: Array<Any>): Boolean {
        return handlerList[type]!!.keys.any {
            keys.contains(it)
        }
    }

    fun register(
        type: KClass<T>,
        identifiers: Array<*>,
        predicates: Array<PluginPredicate<T>> = emptyArray(),
        options: Array<String> = emptyArray(),
        block: (T) -> Unit
    ) {

        if (!existsType(type)) {
            handlerList[type] = mutableMapOf()
        }

        identifiers.forEach { id ->
            val handler = handlerList[type]!!
            val pluginData = PluginData(options, predicates, block)
            if (handler[id].isNullOrEmpty()) {
                handler[id as Any] = arrayListOf(pluginData)
            } else {
                handler[id]!!.add(pluginData)
            }
        }
    }

}