import com.hyze.extensions.message
import com.hyze.plugins.dialogue.DialogueExtension.startDialogue
import com.hyze.plugins.event.ObjectPlugin
import com.rs.game.world.entity.player.content.skills.Skills
import com.rs.game.world.entity.updating.impl.Animation

/*
* UnityScape Plugin Script
* @author Jovic
* @date 03/01/2025  
*/
enum class Stalls(val id: Int, val items: IntArray, val level: Int, val experience: Double) {
    FISH_STALL(211730, intArrayOf(379, 385, 373, 7946, 391, 1963, 315, 319, 325, 347, 361, 365, 333, 329, 351, 339), 30, 50.0),
    VEG_STALL(4706, intArrayOf(1739, 1737, 1779, 1776, 6287, 1635, 1734, 1623, 1625, 1627, 1629, 1631, 1621, 1619, 1617), 1, 25.0),
    TEA_STALL(211734, intArrayOf(1391, 2357), 101, 75.0),
    GEM_STALL(211731, intArrayOf(1391, 2358), 80, 85.0);

    companion object {
        fun getStallsIds() = entries.map { it.id }.toTypedArray()
        fun getById(objectId: Int) = entries.firstOrNull { it.id == objectId }!!
    }
}

val levelPredicate = predicate<ObjectPlugin> {
    successIf { player.skills.getLevel(Skills.THIEVING) > Stalls.getById(worldObject.id).level }
    onFailure { player.message("You need level ${Stalls.getById(worldObject.id).level} to steal this.", red = true) }
}

val inventoryPredicate = predicate<ObjectPlugin> {
    successIf { player.inventory.hasFreeSlots() }
    onFailure { player.startDialogue(1) {
        plain("")
    } }
}

val stealAnimation = Animation(881)

on<ObjectPlugin>(
    identifiers = Stalls.getStallsIds(),
    options = arrayOf("Steal-from"),
    predicates = arrayOf(levelPredicate, inventoryPredicate)
) {
    val stall = Stalls.getById(worldObject.id)

    player.setNextAnimationNoPriority(stealAnimation)
    player.lock(2)

    player.inventory.addItem(stall.items.random(), 1)
    player.skills.addXp(Skills.THIEVING, stall.experience)
}