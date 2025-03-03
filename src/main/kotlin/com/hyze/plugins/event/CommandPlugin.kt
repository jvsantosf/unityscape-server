package com.hyze.plugins.event

import com.hyze.plugins.Plugin
import com.rs.game.world.entity.player.Player

class CommandPlugin(
    player: Player,
    identifiers: Array<Any>,
    val name: String,
    val arguments: Array<String>,
    val console: Boolean = false,
    val clientCommand: Boolean = false
): Plugin(player, identifiers)