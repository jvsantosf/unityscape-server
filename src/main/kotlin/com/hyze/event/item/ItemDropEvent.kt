package com.hyze.event.item

import com.hyze.event.Event
import com.rs.game.item.FloorItem

class ItemDropEvent(
    val item: FloorItem
) : Event()