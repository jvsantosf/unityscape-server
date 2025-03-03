import com.hyze.content.systems.Achievements
import com.hyze.controller.Controller
import com.hyze.controller.ControllerManager
import com.hyze.event.player.PlayerJoinEvent
import com.hyze.extensions.get
import com.hyze.extensions.sendWorldMessage
import com.rs.game.world.World
import com.rs.game.world.entity.player.content.PlayerLook

/*
* UnityScape Plugin Script
* @author Jovic
* @date 01/02/2025  
*/
event<PlayerJoinEvent> {
    player.sendWorldMessage("<col=ede67e>Bem vindo ao UnityScape</col>")
    val activated = if (World.doubleexp) "<col=09ed28>ativado</col>" else "<col=c9475f>desativado</col>"
    player.sendWorldMessage("<col=7eedde>O servidor está com o dobro de EXP</col> $activated")
}

event<PlayerJoinEvent> {
    val starting: Boolean = player["starting", true]
    val definedAppearance: Boolean = player["appearance", false]
    val definedMode: Boolean = player["modes", false]

    if (!starting) {
        return@event
    }

    //player.lock()

    if (!definedAppearance) {
        PlayerLook.openCharacterCustomizing(player)
        return@event
    }
}