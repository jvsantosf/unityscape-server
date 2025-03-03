package com.hyze.plugins

import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
    displayName = "PluginScript",
    fileExtension = "plugin.kts",
    compilationConfiguration = PluginCompilationConfiguration::class,
    evaluationConfiguration = PluginEvaluationConfiguration::class
)
abstract class PluginScript