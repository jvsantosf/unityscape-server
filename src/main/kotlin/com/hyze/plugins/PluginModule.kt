package com.hyze.plugins

import com.hyze.plugins.event.CommandPlugin
import com.hyze.plugins.event.InterfacePlugin
import com.hyze.plugins.event.NPCPlugin
import com.hyze.plugins.event.ObjectPlugin
import com.hyze.utils.inject
import org.koin.dsl.module

val npcManager: PluginEventsManager<NPCPlugin> by inject()
val objectManager: PluginEventsManager<ObjectPlugin> by inject()
val interfaceManager: PluginEventsManager<InterfacePlugin> by inject()
val commandManager: PluginEventsManager<CommandPlugin> by inject()

val pluginModule = module {
    single<PluginEventsManager<ObjectPlugin>> { PluginEventsManager() }
    single<PluginEventsManager<NPCPlugin>> { PluginEventsManager() }
    single<PluginEventsManager<InterfacePlugin>> { PluginEventsManager() }
    single<PluginEventsManager<CommandPlugin>> { PluginEventsManager() }
}

