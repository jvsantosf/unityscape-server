package com.hyze.event.player

import com.hyze.event.Event
import com.rs.game.world.entity.player.Player

class PlayerCommandEvent(
    val player: Player,
    val command: String,
    val console: Boolean = false,
    val client: Boolean = false
): Event()