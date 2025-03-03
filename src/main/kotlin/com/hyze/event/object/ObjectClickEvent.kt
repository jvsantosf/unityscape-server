package com.hyze.event.`object`

import com.hyze.event.PlayerEvent
import com.rs.game.map.WorldObject
import com.rs.game.world.entity.player.Player

class ObjectClickEvent(
    val worldObject: WorldObject,
    player: Player,
    val option: Int,
    val optionName: String
): PlayerEvent(player)