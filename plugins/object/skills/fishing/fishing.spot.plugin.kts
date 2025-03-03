import com.hyze.extensions.message
import com.hyze.plugins.dialogue.DialogueExtension.startDialogue
import com.hyze.plugins.event.ObjectPlugin
import com.rs.game.map.Position
import com.rs.game.world.entity.player.content.FadingScreen
import com.rs.game.world.entity.player.content.skills.Skills

/*
* UnityScape Plugin Script
* @author Jovic
* @date 04/02/2025  
*/
private val ADVANCED_FISHING_SPOT: Position = Position.of(1413, 5529, 0)

val levelPredicate = predicate<ObjectPlugin> {
    successIf { player.skills.getLevel(Skills.FISHING) >= 80 }
    onFailure {
        player.message("Você não tem todos os requisitos para acessar essa área", red = true)
        player.startDialogue {
            plain("Você precisa ter ao menos nível <col=F54272>80</col> em pesca para ir a área avançada de pescaria.")
        }
    }
}

on<ObjectPlugin>(
    identifiers = arrayOf(230108),
    options = arrayOf("Board"),
    predicates = arrayOf(levelPredicate)
) {
    FadingScreen.fade(player) {
        player.nextPosition = ADVANCED_FISHING_SPOT
        player.message("Você velejou até a área avançada de pesca")
    }
}