package com.hyze.plugins

import com.rs.game.world.entity.player.Player

abstract class Plugin(
    val player: Player,
    val identifiers: Array<Any>,
    val options: Array<String> = emptyArray()
)
