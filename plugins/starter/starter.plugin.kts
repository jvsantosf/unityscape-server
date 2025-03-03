import com.hyze.event.player.PlayerJoinEvent
import com.hyze.extensions.get
import com.hyze.extensions.message
import com.hyze.extensions.set
import com.rs.game.world.entity.player.content.PlayerLook

/*
* UnityScape Plugin Script
* @author Jovic
* @date 19/02/2025  
*/
event<PlayerJoinEvent> {
    val isStarting = player["starter", true]
    val stage = player["starter_stage", -1]

    if (isStarting) {
        when (stage) {
            -1 -> PlayerLook.openCharacterCustomizing(player)
            0 -> {
                player.message("Você acaba de acordar, fale com o guia na ponta da ilha para mais informações")
            }
        }
    }
}