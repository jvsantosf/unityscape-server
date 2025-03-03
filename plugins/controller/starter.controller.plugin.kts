import com.hyze.controller.Controller
import com.hyze.controller.IController
import com.hyze.controller.startController
import com.hyze.event.npc.NPCClickEvent
import com.hyze.event.`object`.ObjectClickEvent
import com.hyze.extensions.message
import com.hyze.extensions.player.removeTrail
import com.hyze.extensions.player.startTrail
import com.hyze.plugins.event.CommandPlugin
import com.rs.game.map.Position
import com.rs.game.world.entity.player.Player

/*
* UnityScape Plugin Script
* @author Jovic
* @date 26/02/2025  
*/

private val GUIDE_POSITION = Position(1363, 5517, 0)
private val OLD_WARRIOR_POSITION = Position(1361, 5528, 0)
private val BOAT_POSITION = Position(1363, 5532, 0)

fun controller(player: Player) = IController.build(player) {
    stage {
        whenStart { player.startTrail(GUIDE_POSITION, floorDistance = 2) }
        whenEnd { player.removeTrail() }
        listen<NPCClickEvent> {
            if (npc.id == 945) {
                player.message("Você completou a primeira parte do tutorial!")
                complete(player)
            }
        }
    }
    stage {
        whenStart { player.startTrail(BOAT_POSITION) }
        whenEnd { player.removeTrail() }
        listen<ObjectClickEvent> {
            if (worldObject.definitions.name.equals("Boaty", true)) {
                player.message("Você completou a segunda parte do tutorial!")
                complete(player)
            }
        }
    }
}

on<CommandPlugin>(
    identifiers = arrayOf("controller")
) {
    player.startController(controller(player))
}