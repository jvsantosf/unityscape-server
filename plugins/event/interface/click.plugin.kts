import com.hyze.event.menu.InterfaceClickEvent
import com.hyze.plugins.event.InterfacePlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 04/01/2025  
*/
event<InterfaceClickEvent> {
    interfaceManager.dispatch(
        InterfacePlugin(
            interfaceId = interfaceId,
            componentId = componentId,
            packetId = packetId,
            slotId = slotId,
            slotId2 = slotId2,
            player = player,
            identifiers = arrayOf(interfaceId)
        )
    )
}