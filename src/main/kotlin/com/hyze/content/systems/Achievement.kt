package com.hyze.content.systems

import org.yaml.snakeyaml.Yaml
import java.io.FileReader
import java.io.Serializable


enum class AchievementType(var title: String, val componentId: Int, val tabId: Int) {
    RAIDS_AND_DUNGEONS("Raids & Dungeons", 105, 32),
    SKILLING("Skilling", 106, 33),
    ACTIVITIES("Activities", 107, 34),
    FRIENDSHIP("Friendship", 108, 35),
    SMELTING("Smelting", 109, 36),
    COMBAT_PVM("Combat / PvM", 113, 37);

    companion object {
        val SELECTION_MENU_IDS = intArrayOf(105, 106, 107, 108, 109)
        val TAB_SELECTION_MENU_IDS = intArrayOf(32, 33, 34, 35, 36, 37)
        val TAB_TASKS_IDS = intArrayOf(2, 5, 8, 11, 14, 17)
        val TASK_COMPONENT_IDS = arrayOf(
            19, 22, 25, 28, 31, 34,
            38, 41, 44, 47, 50, 53,
            57, 60, 63, 66, 69, 72
        )

        fun getByComponent(componentId: Int): AchievementType =
            entries.firstOrNull { it.componentId == componentId } ?: COMBAT_PVM

        fun getByComponentTab(componentId: Int): AchievementType =
            entries.firstOrNull { it.tabId == componentId } ?: COMBAT_PVM
    }
}

class Achievement(
    var key: String,
    var type: AchievementType,
    var name: String,
    var description: String,
    var hint: String,
    var rewards: String,
    var iconId: Int,
    var mainSelectionId: Int,
    var mainIconId: Int,
    var selectionId: Int,
    var tabComponentId: Int,
    var tabIconId: Int
): Serializable {
    companion object {
        fun fromMap(map: Map<String, Any>): Achievement {
            return Achievement(
                key = map["key"] as String,
                type = AchievementType.valueOf(map["type"] as String),
                name = map["name"] as String,
                description = map["description"] as String,
                hint = map["hint"] as String,
                rewards = map["rewards"] as String,
                iconId = map["iconId"] as Int,
                mainSelectionId = map["mainSelectionId"] as Int,
                mainIconId = map["mainSelectionIconId"] as Int,
                selectionId = map["selectionIconId"] as Int,
                tabComponentId = map["tabComponentId"] as Int,
                tabIconId = map["tabIconId"] as Int,
            )
        }
    }
}

object Achievements {
    private var achievements: Map<String, Achievement> = emptyMap()

    init {
        val yaml = Yaml()
        val data: Map<String, Map<String, Achievement>> = yaml.load(FileReader("achievement.yaml"))
        achievements = data.mapValues { (_, value) ->
            Achievement.fromMap(value) // Converte cada entrada para Achievement
        }
    }

    fun all() = achievements

    fun first(): Achievement = achievements.values.first()

    operator fun get(key: String): Achievement? = achievements[key]

    fun getBySelectionId(componentId: Int): Achievement? {
       return all().values.firstOrNull { it.mainSelectionId == componentId }
    }

    fun getByTaskIds(componentId: Int): Achievement? {
        return all().values.firstOrNull { it.tabComponentId == componentId }
    }


}