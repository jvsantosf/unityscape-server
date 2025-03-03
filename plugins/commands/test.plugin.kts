import com.hyze.plugins.event.CommandPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
on<CommandPlugin>(
    identifiers = arrayOf("progress")
) {
    player.interfaceManager.sendInterface(3017)
    player.packets.sendRunScript(6257, arguments[1].toInt(), arguments[0].toInt())
}