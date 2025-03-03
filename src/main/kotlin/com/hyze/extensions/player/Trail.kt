package com.hyze.extensions.player

import com.hyze.extensions.get
import com.hyze.extensions.set
import com.rs.game.map.Position
import com.rs.game.map.WorldObject
import com.rs.game.world.entity.Entity
import com.rs.game.world.entity.player.Player

/**
 * Indicators - Appear below the player, it rotates towards
 * the trail direction
 *
 * @param modelId model id of the indicator
 */
enum class Indicator(val modelId: Int) {
    DOTTED(18536),
    DEFAULT(40497)
}

/**
 * Hint - Arrow type that will be shown to the player
 *
 * @param value arrow index
 */
enum class Hint(val value: Int) {
    YELLOW_TRANSPARENT(1),
    BLUE(2),
    GREEN(3),
    YELLOW(4),
    RED(5),
    BLUE_GRADIENT(6),
    WHITE(7),
    RED_GRADIENT(8),
    RED_BORDERED(9),
    SMALL_RED_BORDERED(10);
}

/**
 * Starts a trail based on a world object position
 *
 * @param worldObject object that the trail will start of
 * @param hint arrow color type
 * @param indicator indicator type
 * @param save if it will appear when player re-log in
 * @param floorDistance floor distance, multiply by 64 that it will be +1 on plane
 * @param direction direction of the trail
 */
fun Player.startTrail(worldObject: WorldObject, hint: Hint = Hint.YELLOW, indicator: Indicator = Indicator.DOTTED, save: Boolean = false, floorDistance: Int = 1, direction: Int = 0) {
    this.startTrail(worldObject.position, hint, indicator, save, floorDistance * 64, direction)
}

/**
 * Starts a trail based on a @see {@link Position}
 *
 * @param position position that the trail will start of
 * @param hint arrow color type
 * @param indicator indicator type
 * @param save if it will appear when player re-log in
 * @param floorDistance floor distance, multiply by 64 that it will be +1 on plane
 * @param direction direction of the trail
 */
fun Player.startTrail(position: Position, hint: Hint = Hint.YELLOW, indicator: Indicator = Indicator.DOTTED, save: Boolean = false, floorDistance: Int = 1, direction: Int = 0) {
    val index = hintIconsManager.addHintIcon(position, floorDistance * 64, direction, hint.value, indicator.modelId, save)
    this["hint_icon_$index"] = true
    this["last_hint_icon"] = index
}

/**
 * Starts a trail based on a @see {@link Entity}
 *
 * @param entity entity that the trail will start and follow
 * @param hint arrow color type
 * @param indicator indicator type
 * @param save if it will appear when player re-log in
 * @param floorDistance floor distance, multiply by 64 that it will be +1 on plane
 * @param direction direction of the trail
 */
fun Player.startTrail(entity: Entity, hint: Hint = Hint.YELLOW, indicator: Indicator = Indicator.DOTTED, save: Boolean = false, floorDistance: Int = 1, direction: Int = 0) {
    val index = hintIconsManager.addHintIcon(entity, floorDistance * 64, direction, hint.value, indicator.modelId, save)
    this["hint_icon_$index"] = true
    this["last_hint_icon"] = index
}

/**
 * Check if the player has a trail on
 *
 * @return true if last trail is != -1
 */
fun Player.hasTrail(): Boolean {
    val lastId = this["last_hint_icon"] ?: -1
    return lastId != -1
}

/**
 * Remove the last trail from the player
 */
fun Player.removeTrail() {
    if (hasTrail()) {
        removeTrail(this["last_hint_icon"]!!)
    }
}

/**
 * Remove a trail by given index
 *
 * @param index trail index
 */
fun Player.removeTrail(index: Int) {
    if (hasTrail()) {
        hintIconsManager.removeHintIcon(index)
        val lastId: Int = this["last_hint_icon"]!!
        this["hint_icon_$index"] = false
        this["last_hint_icon"] = lastId - 1
    }
}