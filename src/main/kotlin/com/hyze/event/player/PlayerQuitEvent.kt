package com.hyze.event.player

import com.hyze.event.Event
import com.rs.game.world.entity.player.Player

class PlayerQuitEvent(
    val player: Player
) : Event() {
}