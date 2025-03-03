import com.hyze.event.npc.NPCClickEvent
import com.hyze.extensions.getCurrentController
import com.hyze.extensions.hasController
import com.hyze.plugins.event.NPCPlugin
import com.rs.game.world.entity.npc.others.LivingRock
import com.rs.game.world.entity.player.CoordsEvent
import com.rs.game.world.entity.player.actions.skilling.Fishing
import com.rs.game.world.entity.player.actions.skilling.mining.LivingMineralMining

/*
* UnityScape Plugin Script
* @author Jovic
* @date 30/12/2024  
*/
event<NPCClickEvent> {
    npcManager.dispatch(
        NPCPlugin(
            npc = npc,
            option = optionName,
            identifiers = arrayOf(npc.id, npc.name),
            options = npc.definitions.options,
            player = player,
            isWalkTo = walkTo
        )
    )
}