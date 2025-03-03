import com.hyze.plugins.dialogue.DialogueExtension.startDialogue
import com.hyze.plugins.event.NPCPlugin
import com.rs.game.map.Position
import com.rs.game.world.entity.player.content.Magic

/*
* UnityScape Plugin Script
* @author Jovic
* @date 03/01/2025  
*/
enum class Teleports(
    val title: String,
    val tile: Position
) {
    NEX("Nex", Position(2905, 5203, 0)),
    BANDOS("Bandos", Position(2870, 5363, 2)),
    SARADOMIN("Saradomin", Position(2901, 5264, 0)),
    TORMENTED_DEMONS("Tormented Demons", Position(2562, 5739, 0))
}

on<NPCPlugin>(
    identifiers = arrayOf("Mr Ex"),
    options = arrayOf("Talk-to")
) {
    player.startDialogue(npc.id) {
        npc("Olá, posso teletransportar você para o mundo todo! Para onde deseja ir?")
        options {
            Teleports.entries.forEach { teleport ->
                option(teleport.title) {
                    Magic.sendNormalTeleportSpell(player, 0, 0.0, teleport.tile)
                }
            }
        }
    }
}