package com.hyze.event.player

import com.hyze.event.Event
import com.rs.game.world.entity.player.Player

class PlayerJoinEvent(
    val player: Player
): Event()