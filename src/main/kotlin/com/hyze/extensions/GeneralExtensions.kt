package com.hyze.extensions

import com.rs.game.item.Item

fun Array<Item>.pickRandom(amount: Int = 1): Item {
    return random()
}