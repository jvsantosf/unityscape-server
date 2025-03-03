import com.hyze.extensions.message
import com.hyze.plugins.event.ObjectPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
on<ObjectPlugin>(
    identifiers = arrayOf("Mysterious Cauldron"),
    options = arrayOf("Doar")
) {
    player.message("Em breve...")
}