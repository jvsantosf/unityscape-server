/*
 * RUNESCAPE PRIVATE SERVER FRAMEWORK
 *
 * This file is part of the Hyze Server
 *
 * Hyze is a private RuneScape server focused primarily on
 * in the Brazilian community. The project has only 1 developer
 *
 * Objective of the project is to bring the best content, performance ever seen
 * by brazilians players in relation to private RuneScape servers (RSPS).
 */

package com.hyze

import com.hyze.event.EventBus
import com.hyze.event.server.ServerStartEvent
import com.hyze.plugins.PluginManager
import com.hyze.plugins.pluginModule
import com.hyze.utils.Logger
import com.rs.Launcher
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


/**
 * Server starting
 *
 * @author Async
 * @date 17/07/2020 at 18:01
 */
object Engine {

    lateinit var eventBus: EventBus

    @JvmStatic
    fun main(vararg args: String) {
        val start = System.currentTimeMillis()
        Logger.warn("Starting server!")

        Logger.warn("Setup dependencies injection")
        startKoin {
            printLogger(Level.INFO)
            modules(pluginModule)
        }

        Logger.debug("Starting event bus...")
        eventBus = EventBus()

        Logger.debug("Loading plugins...")
        PluginManager.loadPlugins()
        eventBus.callEvent(ServerStartEvent())

        Logger.debug("Loading server...")
        Launcher.init()

        val end = System.currentTimeMillis()
        Logger.debug("Server took ${end - start}ms to start")
    }

}