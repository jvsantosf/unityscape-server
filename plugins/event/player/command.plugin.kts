import com.hyze.event.player.PlayerCommandEvent
import com.hyze.event.player.PlayerMessageEvent
import com.hyze.plugins.event.CommandPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
event<PlayerCommandEvent> {
    val parts = command.split(" ")
    val command = parts[0]
    val arguments = parts.drop(1).toTypedArray()

    commandManager.dispatch(CommandPlugin(
        player = player,
        identifiers = arrayOf(command),
        name = command,
        arguments = arguments,
        console = false,
        clientCommand = true
    ))
}