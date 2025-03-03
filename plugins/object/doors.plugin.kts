import com.hyze.plugins.event.ObjectPlugin
import com.rs.game.map.WorldObject
import com.rs.game.world.World

/*
* UnityScape Plugin Script
* @author Jovic
* @date 03/01/2025  
*/

on<ObjectPlugin>(
    identifiers = arrayOf("Door"),
    options = arrayOf("Open")
) {
    val openedDoor = WorldObject(
        worldObject.id,
        worldObject.type,
        worldObject.rotation + 1,
        worldObject.x,
        worldObject.y, worldObject.z
    )
    when (worldObject.rotation) {
        0 -> openedDoor.moveLocation(-1, 0, 0)
        1 -> openedDoor.moveLocation(0, 1, 0)
        2 -> openedDoor.moveLocation(1, 0, 0)
        3 -> openedDoor.moveLocation(0, -1, 0)
    }
    if (World.removeTemporaryObject(worldObject, 60000, true)) {
        player.faceObject(openedDoor)
        World.spawnTemporaryObject(openedDoor, 60000, true)
    }
}