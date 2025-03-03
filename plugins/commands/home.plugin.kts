import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin
import com.rs.Constants
import com.rs.game.world.entity.player.content.Magic

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
on<CommandPlugin>(
    identifiers = arrayOf("home")
) {
    player.message("Você está sendo teletransportado para a home")
    Magic.sendNormalTeleportSpell(
        player, 0, 0.0,
        Constants.HOME_PLAYER_LOCATION[0]
    )
}