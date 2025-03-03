import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
on<CommandPlugin>(
    identifiers = arrayOf("donatorzone", "dz")
) {
    player.message("Teste")
}