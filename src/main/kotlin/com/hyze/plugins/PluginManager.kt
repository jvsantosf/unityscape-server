package com.hyze.plugins

object PluginManager {

    fun loadPlugins() {
        PluginScriptHost.loadAndExecuteScripts()
    }
}