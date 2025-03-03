package com.hyze.controller

import com.google.common.collect.Maps
import com.hyze.event.Event
import com.hyze.event.EventBus
import com.hyze.event.Listener
import com.hyze.extensions.getCurrentController
import com.hyze.extensions.message
import com.hyze.extensions.set
import com.hyze.plugins.event
import com.hyze.utils.IndexedQueue
import com.rs.game.world.entity.player.Player

typealias Block<T> = (Player) -> T

interface IController {
    fun build(player: Player): Controller

    companion object {
        fun build(player: Player, block: Controller.() -> Unit): Controller {
            val controller = Controller(player)
            controller.block()
            return controller
        }
    }
}

data class ControllerStage(
    var start: Block<Unit> = {},
    var end: Block<Unit> = {},
) {
    val bus = EventBus()

    fun whenStart(block: Block<Unit>) {
        this.start = block
    }

    fun whenEnd(block: Block<Unit>) {
        this.end = block
    }

    fun complete(player: Player) {
        val controller = player.getCurrentController() ?: return
        end.invoke(player)
        controller.callNextStage(player)
    }

    inline fun <reified T : Event> listen(noinline block: T.() -> Unit) {
        bus.registerListener(T::class, object : Listener<T> {
                override fun handle(event: T) {
                    println("Controller event invoked")
                    block.invoke(event)
                }
            })
        println("Listen method")
    }

    companion object {
        fun create(block: ControllerStage.() -> Unit): ControllerStage {
            val stage = ControllerStage()
            stage.block()

            return stage
        }
    }
}

class Controller(
    private var player: Player,
    private var queue: IndexedQueue<ControllerStage> = IndexedQueue(),
) {

    private var currentStage = 1

    fun getEventBuses(): List<EventBus> {
        return queue.all().map { it.bus }
    }

    fun stage(block: ControllerStage.() -> Unit) {
        val stage = ControllerStage()
        stage.block()
        queue.offer(stage)
    }

    fun callNextStage(player: Player) {
        val stage = queue.poll()

        if (stage == null) {
            player["current_controller_key"] = currentStage
            println("Nullable stage found")
            return
        }

        currentStage = queue.getCurrentIndex()
        stage.start.invoke(player)
        println("Stage#start invoked")
    }

    companion object {
        fun create(player: Player, block: Controller.() -> Unit): Controller {
            val controller = Controller(player)
            controller.block()
            return controller
        }
    }
}

object ControllerManager {
    private val map = Maps.newConcurrentMap<String, Controller>()

    fun put(key: String, controller: Controller) {
        map[key] = controller
        println("New controller added to the map $key")
    }

    fun exists(key: String): Boolean {
        return map.containsKey(key)
    }

    fun size(): Int {
        return map.size
    }

    fun keys(): List<String> = map.keys.toList()
}

fun Player.startController(controller: Controller) {
    transientAssignments.assign("current_controller", controller)
    controller.callNextStage(this)
}