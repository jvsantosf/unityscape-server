package com.hyze.plugins

import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

object PluginCompilationConfiguration : ScriptCompilationConfiguration({
    defaultImports("com.hyze.plugins.dialogue.*", "com.hyze.plugins.commands.*", "com.hyze.plugins.*")
    jvm {
        dependenciesFromCurrentContext(wholeClasspath = true)
        compilerOptions(
            "-jvm-target", "21"
        )
    }
    ide { acceptedLocations(ScriptAcceptedLocation.Everywhere) }
})

object PluginEvaluationConfiguration : ScriptEvaluationConfiguration({

})