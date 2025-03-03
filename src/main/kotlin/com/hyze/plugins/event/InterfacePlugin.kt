package com.hyze.plugins.event

import com.hyze.plugins.Plugin
import com.rs.game.world.entity.player.Player

class InterfacePlugin(
    val interfaceId: Int,
    val componentId: Int,
    val packetId: Int,
    val slotId: Int,
    val slotId2: Int,
    player: Player,
    identifiers: Array<Any>,
): Plugin(player, identifiers)