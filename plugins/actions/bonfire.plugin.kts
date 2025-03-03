import com.hyze.content.systems.action
import com.hyze.content.systems.actionPredicate
import com.hyze.extensions.message
import com.hyze.extensions.playAction
import com.hyze.extensions.schedule
import com.hyze.plugins.event.ObjectPlugin
import com.rs.game.map.Position
import com.rs.game.map.WorldObject
import com.rs.game.world.World
import com.rs.game.world.entity.npc.others.FireSpirit
import com.rs.game.world.entity.player.BuffManager
import com.rs.game.world.entity.player.actions.skilling.Firemaking
import com.rs.game.world.entity.player.content.skills.Skills
import com.rs.game.world.entity.updating.impl.Animation
import com.rs.game.world.entity.updating.impl.Graphics
import com.rs.utility.Utils
import java.awt.Color

/*
* UnityScape Plugin Script
* @author Jovic
* @date 16/02/2025  
*/
enum class Log(
    val logId: Int,
    val gfx: Int,
    val level: Int = 1,
    val experience: Double = 50.0,
    val boostTime: Int = 6
) {
    LOG(1511, 3098, 1, 50.0, 6),
    OAK(1521, 3099, 15, 75.0, 12),
    WILLOW(1519, 3101, 30, 112.5, 18),
    MAPLE(1517, 3100, 45, 157.0, 36),
    EUCALYPTUS(12581, 3112, 58, 241.0, 48),
    YEWS(1515, 3111, 60, 252.0, 54),
    MAGIC(1513, 3135, 75, 378.0, 60),
    BLISTERWOOD(21600, 3113, 76, 379.0, 60),
    CURSED_MAGIC(13567, 3116, 82, 382.0, 60);

    companion object {
        private val ids = entries.map { it.logId }
        fun isLog(itemId: Int) = ids.contains(itemId)
    }
}

on<ObjectPlugin>(
    identifiers = arrayOf("Fire"),
    options = arrayOf("Add-logs")
) {
    val possibilities = Log.entries.filter { log ->
        player.inventory.containsItem(log.logId, 1)
    }

    when (possibilities.size) {
        0 -> player.message("Você não tem nenhuma lenha para adicionar a esta fogueira.")
        1 -> player.playAction("bonfire_action", data = arrayOf(worldObject, possibilities[0]))
        else -> player.dialogueManager.startDialogue("BonfireD", possibilities.toTypedArray(), worldObject)
    }
}

val worldObjectPredicate = actionPredicate {
    successIf { _, data ->
        val worldObject = data[0] as WorldObject
        return@successIf World.containsObjectWithId(worldObject, worldObject.id)
    }
}

val playerHasItemPredicate = actionPredicate {
    successIf { player, data ->
        val log = data[1] as Log
        return@successIf player.inventory.containsItem(log.logId, 1)
    }
    onFailure { player, _ -> player.message("Você não tem mais lenha para queimar nessa fogueira", red = true) }
}

val playerHasLevelPredicate = actionPredicate {
    successIf { player, data ->
        val log = data[1] as Log
        return@successIf player.skills.getLevel(Skills.FIREMAKING) >= log.level
    }
    onFailure { player, _ ->
        player.message("Você não tem nível o suficiente para queimar essa lenha", red = true)
    }
}

action("bonfire", predicates = arrayOf(worldObjectPredicate, playerHasItemPredicate, playerHasLevelPredicate)) {
    var count = 0

    onStart {
        appearence.renderEmote = 2498
    }

    onProcess {
        if (Utils.getRandom(500) == 0) {
            val worldObject = retrieveData<WorldObject>(0)


            FireSpirit(Position(worldObject, 1), this)
            packets.sendTileMessage(
                "Um espírito do fogo emerge da fogueira para lhe recompensar",
                worldObject,
                Color.white.rgb
            )
        }
    }

    onProcessDelayed(6) {
        val log = retrieveData<Log>(1)


        if (equipment.wearingSkillCape(Skills.FIREMAKING) && Utils.getRandom(100) <= 50) {
            message("Sua capa de Arte do Fogo economiza a você uma lenha")
        } else {
            inventory.deleteItem(log.logId, 1)
        }
        skills.addXp(Skills.FIREMAKING, Firemaking.increasedExperience(this, log.experience))
        animate(Animation(16703))
        setNextGraphics(Graphics(log.gfx))
        randomevent(this)
        packets.sendGameMessage("Você adiciona uma lenha ao fogo")
        if (count++ == 4 && lastBonfire == 0) {
            buffManager.applyBuff(BuffManager.Buff(BuffManager.BuffType.BONFIRE, log.boostTime * 100, false));
            message("Você recebeu um fortalecimento que vai durar ${log.boostTime} minutos")
            equipment.refreshConfigs(false)
        }
    }

    onStop {
        emotesManager.nextEmoteEnd = 2400
        schedule(3) {
            animate(Animation(16702))
            appearence.renderEmote = -1
        }
    }
}
