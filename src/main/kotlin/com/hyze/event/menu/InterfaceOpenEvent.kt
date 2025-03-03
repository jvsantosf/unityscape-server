package com.hyze.event.menu

import com.hyze.event.Event
import com.rs.game.world.entity.player.Player

class InterfaceOpenEvent(
    val player: Player,
    val interfaceId: Int,
): Event()