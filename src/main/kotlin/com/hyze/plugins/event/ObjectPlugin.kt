package com.hyze.plugins.event

import com.hyze.plugins.Plugin
import com.rs.game.map.WorldObject
import com.rs.game.world.entity.player.Player

class ObjectPlugin(
    val worldObject: WorldObject,
    val option: String,
    identifiers: Array<Any>,
    player: Player,
    options: Array<String>
): Plugin(player, identifiers, options)