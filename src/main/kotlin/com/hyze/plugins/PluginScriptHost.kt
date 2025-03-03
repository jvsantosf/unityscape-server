package com.hyze.plugins

import com.hyze.utils.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import kotlin.io.path.name
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.BasicScriptingHost
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.createJvmEvaluationConfigurationFromTemplate
import kotlin.system.exitProcess

class PluginScriptHost {
    companion object {
        fun loadAndExecuteScripts() {
            Logger.debug("Loading plugin scripts...")
            var scriptCount = 0

            CompletableFuture.runAsync {
                val allStart = System.currentTimeMillis()
                val host = BasicJvmScriptingHost()

                Files.walk(Paths.get("./plugins/"))
                    .filter { Files.isRegularFile(it) && it.toString().endsWith(".plugin.kts") }
                    .forEach { path ->
                        val start = System.currentTimeMillis()
                        val file = path.toFile()
                        val result = evalFile(file, host)
                        if (result is ResultWithDiagnostics.Failure) {
                            logScriptError(file, result)
                            exitProcess(1)
                        } else {
                            val end = System.currentTimeMillis()
                            Logger.debug("[ ${path.parent.name} ] Loaded plugin script ${file.nameWithoutExtension}     (${end - start}ms)")
                            scriptCount++
                        }
                    }
                val allEnd = System.currentTimeMillis()
                Logger.debug("(${Thread.currentThread().name}) Foram carregados $scriptCount plugins (total: ${allEnd - allStart}ms)")
            }
        }

        fun loadAndExecuteScript(name: String, host: BasicScriptingHost) {
            Files.walk(Paths.get("./plugins/"))
                .filter {
                    Files.isRegularFile(it) && it.toString()
                        .replace(".plugin.kts", "")
                        .contains(name) && it.toString().endsWith(".plugin.kts")
                }
                .forEach { path ->
                    val file = path.toFile()
                    val result = evalFile(file, host)

                    Logger.debug("Plugin ${file.name} foi carregado por algum usuário")

                    if (result is ResultWithDiagnostics.Failure) {
                        logScriptError(file, result)
                        exitProcess(1)
                    }
                }
        }

        private fun evalFile(scriptFile: File, host: BasicScriptingHost): ResultWithDiagnostics<EvaluationResult> {
            return host.eval(
                scriptFile.toScriptSource(),
                createJvmCompilationConfigurationFromTemplate<PluginScript> {
                    defaultImports("com.hyze.plugins.dialogue.*", "com.hyze.plugins.commands.*", "com.hyze.plugins.*")
                    jvm { dependenciesFromCurrentContext(wholeClasspath = true) }
                    ide { acceptedLocations(ScriptAcceptedLocation.Everywhere) }
                },
                createJvmEvaluationConfigurationFromTemplate<PluginScript> {

                })
        }

        private fun logScriptError(file: File, result: ResultWithDiagnostics.Failure) {
            println("Script evaluation failed for '${file.name}':")
            result.reports.forEach { report ->
                println(" - ${report.message} (severity: ${report.severity})")
            }
        }
    }
}