import com.hyze.content.systems.Achievements
import com.hyze.plugins.event.CommandPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 09/02/2025  
*/
on<CommandPlugin>(
    identifiers = arrayOf("achiv")
) {
    player.packets.sendHideIComponent(1055, 0, false)
    player.packets.sendRunScript(3970)
    player.packets.sendRunScript(3969)
    player.packets.sendRunScript(3968)
    player.interfaceManager.sendTab(if (player.interfaceManager.isResizableScreen) 1 else 5, 1055)
    player.packets.sendIComponentText(
        1055, 15, ("Você completou uma missão, parabéns!")
    )
}