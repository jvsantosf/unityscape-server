import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin
import com.hyze.plugins.event.InterfacePlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 18/02/2025  
*/
private enum class Category(val componentId: Int, val configValue: Int) {
    AURA(7, 1),
    COSMETIC_AURAS(8, 9),
    EMOTES(9, 2),
    COSTUMES(10, 3),
    TITLES(11, 4),
    RE_COLOUR(12, 5),
    SPECIAL_OFFERS(13, 6),
    MY_FAVOURITES(3, 8);

    companion object {
        val components = entries.associateBy { it.componentId }
        val componentsIds = entries.map { it.componentId }
        fun getByComponentId(componentId: Int): Category {
            return components[componentId] ?: AURA
        }
    }
}

private val CLOSE_BUTTON = 103

on<InterfacePlugin>(
    identifiers = arrayOf(1143)
) {
    player.message("Você clicou no componente $componentId, $packetId, $slotId, $slotId2")
    when (componentId) {
        59 -> player.packets.sendHideIComponent(1143, 16, true)
        1 -> {
            player.packets.sendHideIComponent(1143, 16, false)
            player.packets.sendHideIComponent(1143, 56, false)
        }
        CLOSE_BUTTON -> player.closeInterfaces()
        in Category.componentsIds -> player.packets.sendConfig(2226, Category.getByComponentId(componentId).configValue)
    }
}

on<CommandPlugin>(
    identifiers = arrayOf("leal")
) {
    player.interfaceManager.sendScreenInterface(96, 1143)
    player.packets.sendGlobalConfig(1648, player.loyaltyPoints)
    for (index in 0..50) {
        player.packets.sendUnlockIComponentOptionSlots(1143, index, 1, 10, true, 1, 2, 3, 4, 5)
    }
}

on<CommandPlugin>(
    identifiers = arrayOf("lealcfg")
) {
    val configType = arguments[0].toInt()
    val configId = arguments[1].toInt()
    val configValue = arguments[2].toInt()

    when (configType) {
        1 -> player.packets.sendConfig(configId, configValue)
        2 -> player.packets.sendConfigByFile(configId, configValue)
        3 -> player.packets.sendGlobalConfig(configId, configValue)
    }
    player.message("A configuração foi enviada para a interface atual [type=$configType, config_id=$configId, config_value=$configValue]")
}