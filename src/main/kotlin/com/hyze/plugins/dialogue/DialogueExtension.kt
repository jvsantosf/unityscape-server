package com.hyze.plugins.dialogue

import com.rs.game.world.entity.player.Player

object DialogueExtension {

    fun Player.createDialogue(npcId: Int, init: DialogueBuilder.() -> Unit): DialogueBuilder {
        val dialoguePlugin = DialogueBuilder(this)
        dialoguePlugin.npcId = npcId
        dialoguePlugin.init()
        return dialoguePlugin
    }

    fun Player.startDialogue(npcId: Int = -1, init: DialogueBuilder.() -> Unit) {
        val dialoguePlugin = DialogueBuilder(this)
        dialoguePlugin.npcId = npcId
        dialoguePlugin.init()
        newDialogueManager.start(object : DialoguePlugin() {
            override fun build(player: Player, npcId: Int): DialogueBuilder {
                return dialoguePlugin
            }
        }, npcId)
    }

    fun Player.createDialogue(init: DialogueBuilder.() -> Unit): DialogueBuilder {
        val dialoguePlugin = DialogueBuilder(this)
        dialoguePlugin.init()
        return dialoguePlugin
    }

}