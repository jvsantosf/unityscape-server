package com.hyze.plugins.event

import com.hyze.plugins.Plugin
import com.rs.game.world.entity.npc.NPC
import com.rs.game.world.entity.player.Player

class NPCPlugin(
    val npc: NPC,
    val option: String,
    val isWalkTo: Boolean,
    identifiers: Array<Any>,
    options: Array<String>,
    player: Player
) : Plugin(player, identifiers, options)