package com.hyze.event

import com.rs.game.world.entity.player.Player

open class PlayerEvent(val player: Player): Event()
open class Event