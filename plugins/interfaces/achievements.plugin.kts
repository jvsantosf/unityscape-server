import com.hyze.content.systems.Achievement
import com.hyze.content.systems.AchievementType
import com.hyze.content.systems.Achievements
import com.hyze.extensions.get
import com.hyze.extensions.set
import com.hyze.plugins.event.InterfacePlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 09/02/2025  
*/
private val INTERFACE_ID = 3212
private val BACK_BUTTON = 83

private val SELECTION_MENU_COMPONENT = 99
private val SELECTION_MENU_TEXT = 100
private val SELECTION_MENU_ROWS = 102

private val ACHIEVEMENT_DETAILS_TYPE_TEXT = 80
private val ACHIEVEMENT_DETAILS_DIFFICULTY_TEXT = 81
private val ACHIEVEMENT_DETAILS_TITLE_TEXT = 82
private val ACHIEVEMENT_DETAILS_DESCRIPTION_TEXT = 96
private val ACHIEVEMENT_DETAILS_ICON_ID = 78
private val ACHIEVEMENT_DETAILS = 111
private val ACHIEVEMENT_TYPES = AchievementType.entries.toTypedArray()

on<InterfacePlugin>(
    identifiers = arrayOf(INTERFACE_ID)
) {
    var hideButton = true
    val selectedType: AchievementType
    var achievement: Achievement?

    when (componentId) {
        BACK_BUTTON -> player.packets.sendHideIComponent(INTERFACE_ID, 111, true)
        SELECTION_MENU_COMPONENT -> {
            player.packets.sendHideIComponent(
                INTERFACE_ID,
                SELECTION_MENU_ROWS,
                hideButton.apply { hideButton = !hideButton })
        }

        in AchievementType.SELECTION_MENU_IDS -> {
            selectedType = AchievementType.getByComponent(componentId)
            player.packets.sendHideIComponent(INTERFACE_ID, SELECTION_MENU_ROWS, true)
            // TODO: Update task list
        }

        in AchievementType.TASK_COMPONENT_IDS -> {
            achievement = Achievements.getBySelectionId(componentId)
            player.packets.sendHideIComponent(INTERFACE_ID, ACHIEVEMENT_DETAILS, false)
            player.packets.sendIComponentText(INTERFACE_ID, ACHIEVEMENT_DETAILS_TITLE_TEXT, achievement?.name)
            player.packets.sendIComponentSprite(INTERFACE_ID, ACHIEVEMENT_DETAILS_ICON_ID, achievement?.iconId ?: 4532)
            player.packets.sendIComponentText(
                INTERFACE_ID,
                ACHIEVEMENT_DETAILS_TYPE_TEXT,
                "Type : ${achievement?.type?.title}"
            )
        }
    }
}

private val TAB_INTERFACE_LIST_ID = 3216
private val TAB_OPEN_ACHIEVEMENTS_LIST = 23
private val TAB_SWITCH_ACHIEVEMENTS_TYPE = 25
private val TAB_SWITCH_ACHIEVEMENT_TEXT = 26
private val TAB_SELECTION_ACHIEVEMENTS_MENU = 29
private val TAB_HOVER_TEXT = 22

on<InterfacePlugin>(
    identifiers = arrayOf(TAB_INTERFACE_LIST_ID)
) {
    val selectedType = AchievementType.getByComponentTab(componentId)
    var hiddenButton: Boolean = player["tab_task_hidden_select", true]
    val lastAchievementKey: String = player["last_viewed_achievement", ""]
    val lastAchievementTab: Int = player["last_viewed_achievement_tab", -1]

    when (componentId) {
        TAB_OPEN_ACHIEVEMENTS_LIST -> {
            Achievements.all().values.forEach {
                player.packets.sendIComponentSprite(INTERFACE_ID, it.mainIconId, it.iconId)
            }
            player.interfaceManager.sendInterface(INTERFACE_ID)
        }
        TAB_SWITCH_ACHIEVEMENTS_TYPE -> {
            player["tab_task_hidden_select"] = !hiddenButton
            player.packets.sendHideIComponent(TAB_INTERFACE_LIST_ID, TAB_SELECTION_ACHIEVEMENTS_MENU, player["tab_task_hidden_select"]!!)
        }

        in AchievementType.TAB_TASKS_IDS -> {
            val achievement = Achievements.getByTaskIds(componentId)
            player["viewing_achievement", achievement]

            if (packetId == 67) {
                player.interfaceManager.sendTab(if (player.interfaceManager.isResizableScreen) 114 else 174, 3215)
                player.packets.sendIComponentText(TAB_TASK_INTERFACE_ID, TAB_TASK_DESCRIPTION_TEXT, achievement?.description)
                player.packets.sendIComponentText(TAB_TASK_INTERFACE_ID, TAB_TASK_TITLE_TEXT, achievement?.name)
                player.packets.sendIComponentSprite(TAB_TASK_INTERFACE_ID, TAB_TASK_ICON_ID, achievement?.iconId ?: 4532)
                return@on
            }

            if (lastAchievementKey.isNotEmpty() && lastAchievementKey != achievement?.key) {
                println("1. last_achievement_key=$lastAchievementKey :: achievement_key=${achievement?.key}")
                player.packets.sendIComponentSprite(TAB_INTERFACE_LIST_ID, lastAchievementTab, 4041) // Desativa o antigo
            }

            if (lastAchievementKey == achievement?.key) {
                println("2. last_achievement_key=$lastAchievementKey :: achievement_key=${achievement.key}")
                player.packets.sendIComponentSprite(TAB_INTERFACE_LIST_ID, (achievement.tabIconId.minus(1)), 4042)
                player["last_viewed_achievement"] = ""
                player["last_viewed_achievement_tab"] = -1
            } else {
                player.packets.sendIComponentSprite(TAB_TASK_INTERFACE_ID, (achievement?.tabIconId?.minus(1)) ?: -1, 4041)
                player["last_viewed_achievement"] = achievement?.key ?: ""
                player["last_viewed_achievement_tab"] = achievement?.tabIconId?.minus(-1) ?: -1
            }

            player.packets.sendIComponentText(
                TAB_INTERFACE_LIST_ID,
                TAB_HOVER_TEXT,
                "${selectedType.title} - ${achievement?.description}"
            )
        }

        in AchievementType.TAB_SELECTION_MENU_IDS -> {
            player.packets.sendIComponentText(TAB_INTERFACE_LIST_ID, TAB_SWITCH_ACHIEVEMENT_TEXT, selectedType.title)
        }
    }
}


private val TAB_TASK_INTERFACE_ID = 3215
private val TAB_TASK_BACK = 5
private val TAB_TASK_TITLE_TEXT = 7
private val TAB_TASK_DESCRIPTION_TEXT = 16
private val TAB_TASK_ICON_ID = 2
private val TAB_HINT_BUTTON = 8
private val TAB_REWARDS_BUTTON = 10
private val TAB_DESCRIPTION_BUTTON = 12
on<InterfacePlugin>(
    identifiers = arrayOf(TAB_TASK_INTERFACE_ID)
) {
    val achievement: Achievement? = player["viewing_achievement", null]

    when (componentId) {
        TAB_TASK_BACK -> player.interfaceManager.sendTab(
            if (player.interfaceManager.isResizableScreen) 114 else 174,
            TAB_INTERFACE_LIST_ID
        )
        TAB_HINT_BUTTON -> player.packets.sendIComponentText(TAB_TASK_INTERFACE_ID, TAB_TASK_DESCRIPTION_TEXT, achievement?.hint)
        TAB_REWARDS_BUTTON -> player.packets.sendIComponentText(TAB_TASK_INTERFACE_ID, TAB_TASK_DESCRIPTION_TEXT, achievement?.rewards)
        TAB_DESCRIPTION_BUTTON -> player.packets.sendIComponentText(TAB_TASK_INTERFACE_ID, TAB_TASK_DESCRIPTION_TEXT, achievement?.description)
    }
}