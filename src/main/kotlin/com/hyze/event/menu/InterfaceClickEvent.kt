package com.hyze.event.menu

import com.hyze.event.Event
import com.rs.game.world.entity.player.Player

class InterfaceClickEvent(
    val player: Player,
    val interfaceId: Int,
    val componentId: Int,
    val packetId: Int,
    val slotId: Int,
    val slotId2: Int,
): Event()