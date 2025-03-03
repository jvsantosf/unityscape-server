package com.hyze.extensions

import com.hyze.content.systems.KtActionManager
import com.hyze.controller.Controller
import com.rs.game.world.entity.player.Player

fun Player.message(message: String, box: Boolean = true, red: Boolean = false) {
    if (box) {
        packets.sendPlayerMessage((if (red) 1 else 0), 15263739, this, message, true)
    } else {
        packets.sendGameMessage(message)
    }
}

fun Player.playAction(key: String, data: Array<Any> = emptyArray()) {
    actionManager.setAction(KtActionManager[key]?.buildAction(data))
}

fun Player.hasController(): Boolean {
    return this.assignments.get("current_controller") != null
}

fun Player.getCurrentControllerStage(): Int {
    return this["current_controller_key"] ?: -1
}

fun Player.getCurrentController(): Controller? {
    return this.transientAssignments.get("current_controller") as Controller?
}

fun Player.sendWorldMessage(message: String) {
    sm(message)
}

operator fun <T> Player.get(key: String, defaultValue: T): T {
    return assignments.assignments.getOrPut(key) {
        this[key] = defaultValue
        return defaultValue
    } as T
}

operator fun <T> Player.get(key: String): T? {
    return assignments.assignments[key] as T?
}


operator fun <T> Player.set(key: String, value: T?) {
    assignments.assign(key, value)
}

fun Player.removeAssignment(key: String) {
    assignments.unassign(key)
}