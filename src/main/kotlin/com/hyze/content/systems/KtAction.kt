package com.hyze.content.systems

import com.google.common.collect.Maps
import com.hyze.plugins.FunctionDSLMarker
import com.hyze.utils.Logger
import com.rs.game.world.entity.player.Player
import com.rs.game.world.entity.player.actions.Action
import org.apache.commons.lang3.RandomUtils
import java.io.File

typealias KtActionFunc<T> = Player.() -> T
typealias KtActionData = Array<Any>

data class KtActionDelayed(val delay: Int, val action: Player.() -> Unit)

data class KtActionPredicate(
    var failure: (Player, KtActionData) -> Unit = { _, _ -> },
    var predicate: (Player, KtActionData) -> Boolean = { _, _ -> true },
) {

    fun onFailure(block: (Player, KtActionData) -> Unit) {
        failure = block
    }

    fun successIf(block: (Player, KtActionData) -> Boolean) {
        predicate = block
    }
}

object KtActionManager {

    private val actions = Maps.newConcurrentMap<String, KtAction>()

    operator fun set(key: String, action: KtAction) {
        actions["${key}_action"] = action
        Logger.debug("An new action was added with the name [${key}_action]")
    }

    operator fun get(key: String) = actions[key]

    fun exists(key: String) = actions.containsKey(key)
}

class KtAction(
    private val name: String,
    private var predicates: Array<KtActionPredicate> = emptyArray(),
    private var data: Array<Any> = emptyArray(),
    private var start: KtActionFunc<Unit> = {},
    private var process: KtActionFunc<Unit> = {},
    private var processDelayed: KtActionDelayed = KtActionDelayed(-1) {},
    private var stop: KtActionFunc<Unit> = {},
) {

    fun <T : Any> retrieveData(index: Int): T {
        val dataObject = data.getOrNull(index) as T?
        requireNotNull(dataObject) { "Data object for index $index was not found for action [${name}]" }
        return dataObject
    }

    fun onStart(block: KtActionFunc<Unit>) {
        start = block
    }

    fun onProcess(block: KtActionFunc<Unit>) {
        process = block
    }

    fun onProcessDelayed(delay: Int, block: Player.() -> Unit) {
        processDelayed = KtActionDelayed(delay, block)
    }

    fun onStop(block: KtActionFunc<Unit>) {
        stop = block
    }

    private fun checkPredicate(player: Player, predicates: Array<KtActionPredicate>): Boolean {
        for (predicate in predicates) {
            if (!predicate.predicate.invoke(player, data)) {
                predicate.failure.invoke(player, data)
                return false
            }
        }

        return true
    }

    fun buildAction(data: Array<Any>): Action {
        this.data = data
        return object : Action() {
            override fun start(player: Player): Boolean {
                if (predicates.isEmpty()) {
                    start.invoke(player)
                    return true
                }

                if (checkPredicate(player, predicates)) {
                    start.invoke(player)
                    return true
                }

                return false
            }

            override fun process(player: Player): Boolean {
                if (predicates.isEmpty()) {
                    process.invoke(player)
                    return true
                }

                if (checkPredicate(player, predicates)) {
                    process.invoke(player)
                    return true
                }

                return false
            }

            override fun processWithDelay(player: Player): Int {
                processDelayed.action.invoke(player)
                return processDelayed.delay
            }

            override fun stop(player: Player) {
                stop.invoke(player)
            }
        }
    }
}

@FunctionDSLMarker
fun action(
    name: String,
    predicates: Array<KtActionPredicate> = emptyArray(),
    block: KtAction.() -> Unit
) {
    val ktAction = KtAction(name, predicates = predicates)
    ktAction.block()
    KtActionManager[name] = ktAction
}

@FunctionDSLMarker
fun actionPredicate(block: KtActionPredicate.() -> Unit): KtActionPredicate {
    val ktActionPredicate = KtActionPredicate()
    ktActionPredicate.block()
    return ktActionPredicate
}