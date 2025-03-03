import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin
import com.rs.cache.loaders.ObjectDefinitions

/*
* UnityScape Plugin Script
* @author Jovic
* @date 01/02/2025  
*/
on<CommandPlugin>(
    identifiers = arrayOf("objmap")
) {
    val id = arguments[0].toInt()

    player.message("mapSpriteId > ${ObjectDefinitions.getObjectDefinitions(id).mapIconId}")
}