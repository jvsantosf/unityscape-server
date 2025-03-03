package com.hyze.event.npc

import com.hyze.event.Event
import com.hyze.event.PlayerEvent
import com.rs.game.world.entity.npc.NPC
import com.rs.game.world.entity.player.Player

class NPCClickEvent(
    val npc: NPC,
    player: Player,
    val option: Int,
    val optionName: String,
    var walkTo: Boolean = false,
) : PlayerEvent(player)