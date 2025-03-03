package com.hyze.event.player

import com.hyze.event.Event
import com.rs.game.item.Item
import com.rs.game.world.entity.player.Player

class PlayerEatFoodEvent(
    val player: Player,
    val food: Item,
    val slot: Int
): Event()