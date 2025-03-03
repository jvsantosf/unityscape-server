import com.hyze.event.menu.InterfaceOpenEvent
import com.hyze.extensions.get
import com.hyze.extensions.message
import com.hyze.plugins.event.InterfacePlugin
import com.rs.game.item.Item
import com.rs.game.item.ItemsContainer
import com.rs.game.world.entity.player.Equipment
import com.rs.game.world.entity.player.Player

/*
* UnityScape Plugin Script
* @author Jovic
* @date 04/01/2025
*/
data class EquipmentItem(val slot: Byte, val item: Item)

enum class LoadoutButton(
    val componentId: Int,
    val money: Int,
    val loyaltyPoints: Int,
    val pvmPoints: Int,
    val items: Array<Item>,
    val starterItems: Array<EquipmentItem>
) {
    MELEE(
        95,
        500000,
        250,
        1200,
        arrayOf(Item(380, 100), Item(11838, 1)),
        arrayOf(
            EquipmentItem(Equipment.SLOT_CAPE, Item(6568, 1)),
            EquipmentItem(Equipment.SLOT_HAT, Item(1153, 1)),
            EquipmentItem(Equipment.SLOT_CHEST, Item(1115, 1)),
            EquipmentItem(Equipment.SLOT_LEGS, Item(1067, 1)),
            EquipmentItem(Equipment.SLOT_WEAPON, Item(1323, 1)),
            EquipmentItem(Equipment.SLOT_HANDS, Item(12988, 1)),
        )
    ),
    MAGE(
        109,
        500000,
        250,
        1200,
        arrayOf(Item(380, 100), Item(554, 200), Item(555, 200),
            Item(556, 200), Item(557, 200), Item(560, 65),
            Item(561, 120), Item(565, 25)),
        arrayOf(
            EquipmentItem(Equipment.SLOT_AMULET, Item(1727, 1)),
            EquipmentItem(Equipment.SLOT_CAPE, Item(23052, 1)),
            EquipmentItem(Equipment.SLOT_WEAPON, Item(1381, 1)),
            EquipmentItem(Equipment.SLOT_HANDS, Item(12988, 1)),
        )
    ),
    ARCHER(
        102,
        500000,
        250,
        1200,
        arrayOf(Item(380, 100), Item(884, 230), Item(892, 120)),
        arrayOf(
            EquipmentItem(Equipment.SLOT_CAPE, Item(23050, 1)),
            EquipmentItem(Equipment.SLOT_HAT, Item(3749, 1)),
            EquipmentItem(Equipment.SLOT_CHEST, Item(1129, 1)),
            EquipmentItem(Equipment.SLOT_LEGS, Item(1095, 1)),
            EquipmentItem(Equipment.SLOT_WEAPON, Item(841, 1)),
            EquipmentItem(Equipment.SLOT_HANDS, Item(1063, 1)),
            EquipmentItem(Equipment.SLOT_FEET, Item(1061))
        )
    ),
    SKILLER(
        116,
        650000,
        390,
        0,
        arrayOf(Item(380, 100), Item(23716, 2), Item(1512, 95), Item(1778, 35)),
        arrayOf(
            EquipmentItem(Equipment.SLOT_CAPE, Item(12645, 1))
        )
    );

    companion object {
        fun getByComponent(id: Int): LoadoutButton? = entries.firstOrNull { it.componentId == id }
    }
}

enum class ModesButton(val componentId: Int, val money: Int, val loyaltyPoints: Int, val pvmPoints: Int) {
    EASY(35, 1000000, 80, 120),
    NORMAL(58, 1500000, 100, 185),
    HARD(73, 2000000, 220, 300);

    companion object {
        fun getByComponent(id: Int): ModesButton? = entries.firstOrNull { it.componentId == id }
    }
}

data class CombinedGameMode(
    var mode: ModesButton? = null,
    var loadout: LoadoutButton? = null,
    var money: Int = 0,
    var loyaltyPoints: Int = 0,
    var pvmPoints: Int = 0,
    var container: ItemsContainer<Item> = ItemsContainer(16, true)
)

val playerModeSelection = hashMapOf<String, CombinedGameMode>()

event<InterfaceOpenEvent> {
    if (interfaceId == 3009) {
        playerModeSelection[player.username] = CombinedGameMode()
    }
}

on<InterfacePlugin>(
    identifiers = arrayOf(3009)
) {
    val data = playerModeSelection[player.username] ?: CombinedGameMode()

    if (componentId == 163) {
        player.packets.sendHideIComponent(3009, 144, true)
        return@on
    }

    if (componentId == 162) {
        player.interfaceManager.closeScreenInterface()
        player.equipment.refresh(
            Equipment.SLOT_HAT.toInt(), Equipment.SLOT_CHEST.toInt(), Equipment.SLOT_LEGS.toInt(),
            Equipment.SLOT_CAPE.toInt(), Equipment.SLOT_HANDS.toInt(), Equipment.SLOT_WEAPON.toInt(),
            Equipment.SLOT_AMULET.toInt(), Equipment.SLOT_FEET.toInt(), Equipment.SLOT_ARROWS.toInt()
        )
        data.container.items.filterNotNull().forEach { player.inventory.addItem(it) }
        player.inventory.refresh()
        player.unlock()
        player["modes", true]
        player["starting", false]
        player.message("Desfrute de novas aventuras e batalhas!")
        return@on
    }

    if (componentId == 123 && data.mode != null && data.loadout != null) {
        player.packets.sendHideIComponent(3009, 144, false)
        return@on
    }

    val selectedMode = ModesButton.getByComponent(componentId)
    if (selectedMode != null) {
        if (data.mode != selectedMode) {
            data.mode = selectedMode
            recalculatePoints(player, data)
        }
    }

    val selectedLoadout = LoadoutButton.getByComponent(componentId)
    if (selectedLoadout != null) {
        if (data.loadout != selectedLoadout) {
            data.loadout = selectedLoadout
            recalculatePoints(player, data)
        }
    }

    playerModeSelection[player.username] = data

    player.packets.sendIComponentText(3009, 57, "${data.money} gp")
    player.packets.sendIComponentText(3009, 129, "${data.loyaltyPoints} Loyalty points")
    player.packets.sendIComponentText(3009, 134, "${data.pvmPoints} PVM points")
}

fun recalculatePoints(player: Player, data: CombinedGameMode) {
    data.money = 0
    data.loyaltyPoints = 0
    data.pvmPoints = 0

    data.container = ItemsContainer<Item>(20, false)
    val money = Item(995, 1)

    data.mode?.let {
        data.money += it.money
        data.loyaltyPoints += it.loyaltyPoints
        data.pvmPoints += it.pvmPoints
        money.amount += it.money
    }
    data.loadout?.let {
        data.money += it.money
        data.loyaltyPoints += it.loyaltyPoints
        data.pvmPoints += it.pvmPoints
        money.amount += it.money

        data.container.addAll(it.items)

        player.packets.sendInterSetItemsOptionsScript(3009, 141, 100, 3, 7, "Examine")
        player.packets.sendUnlockIComponentOptionSlots(3009, 141, 0, 10, 0, 1, 2, 3)
        player.packets.sendItems(100, data.container)

        player.equipment.reset()
        it.starterItems.forEach { item ->
            player.equipment.items.set(item.slot.toInt(), item.item)
        }
        player.appearence.generateAppearenceData()
    }

    data.container.add(money)
}
