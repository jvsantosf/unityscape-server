package com.hyze.event.player

import com.hyze.event.Event
import com.rs.game.world.entity.player.Player

data class PlayerMessageEvent(
    val player: Player,
    val message: String,
): Event()