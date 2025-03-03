import com.hyze.plugins.event.InterfacePlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
on<InterfacePlugin>(
    identifiers = arrayOf(3011)
) {
    if (componentId == 6) {
        player.interfaceManager.sendInventory()
    }
}