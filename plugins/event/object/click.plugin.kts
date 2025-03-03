import com.hyze.event.`object`.ObjectClickEvent
import com.hyze.extensions.getCurrentController
import com.hyze.extensions.hasController
import com.hyze.plugins.event.ObjectPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 03/01/2025  
*/
event<ObjectClickEvent> {
    if (player.hasController()) {
        player.getCurrentController()!!.getEventBuses()
            .onEach { bus -> bus.callEvent(this) }
    }

    objectManager.dispatch(
        ObjectPlugin(
            worldObject = worldObject,
            option = optionName,
            identifiers = arrayOf(worldObject.id, worldObject.definitions.name),
            options = worldObject.definitions.options,
            player = player
        )
    )
}