import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin
import com.hyze.utils.Logger
import com.rs.cache.loaders.ItemDefinitions
import com.rs.game.item.Item
import com.rs.game.world.entity.player.Player
import java.util.*

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
private val totalItems = 33204
private var currentItemId = 32242
private val itemsToAdd = 28

on<CommandPlugin>(
    identifiers = arrayOf("discord", "dc")
) {
    val timer = Timer()
    val task = object : TimerTask() {
        override fun run() {
            player.inventory.reset()
            addItemsInventory(player)
            if (currentItemId >= totalItems) {
                timer.cancel()
                Logger.debug("Todos os itens foram processados. Finalizando timer..")
            }
        }
    }

    timer.scheduleAtFixedRate(task, 0, 1500)
}

fun addItemsInventory(player: Player) {
    for (id in 0..itemsToAdd) {
        val definitions = ItemDefinitions.getItemDefinitions(currentItemId)
        if (definitions.name == "null") {
            Logger.error("Skipping item id $currentItemId, no definitions")
            currentItemId++
            continue
        }

        if (currentItemId < totalItems) {
            Logger.warn("Adicionado item '${definitions.name}' ao inventario")
            player.inventory.addItem(Item(currentItemId, 1))
            currentItemId++
        } else {
            Logger.debug("Todos os itens foram adicionados..")
        }
    }
}