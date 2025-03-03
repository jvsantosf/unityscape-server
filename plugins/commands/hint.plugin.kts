import com.hyze.extensions.message
import com.hyze.extensions.player.Hint
import com.hyze.extensions.player.removeTrail
import com.hyze.extensions.player.startTrail
import com.hyze.plugins.event.CommandPlugin
import com.rs.game.map.Position
import com.rs.game.world.entity.player.HintIcon


/*
* UnityScape Plugin Script
* @author Jovic
* @date 02/02/2025  
*/
on<CommandPlugin>(
    identifiers = arrayOf("hint")
) {

    val position = Position(1348, 5478, 0)
    player.startTrail(position)
    player.message("Uma novo indicador de caminho foi iniciado")
}