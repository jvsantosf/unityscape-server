package com.hyze.plugins.dialogue

import com.hyze.plugins.dialogue.message.OptionMessageDialogue
import com.rs.game.world.entity.player.Player

class DialogueManager(val player: Player) {

    private var lastDialogue: DialogueBuilder? = null

    /**
     * Sends player to the next dialogue stage
     */

    fun next(interfaceId: Int, componentId: Int) {
        if (interfaceId == 1184) {
            lastDialogue?.callNextMessage()
        } else if (interfaceId == 1188) {
            lastDialogue?.callNextMessage(OptionMessageDialogue.Option.getOption(componentId))
        }
    }

    /**
     * Start a player dialogue
     */

    fun start(dialogueBuilder: DialoguePlugin, npcId: Int) {
        if (lastDialogue != null) {
            finish()
        }

        val plugin = dialogueBuilder.build(player, npcId)
        lastDialogue = plugin

        plugin.callMessage()
    }

    /**
     * Finish a dialogue
     */

    fun finish() {
        if (lastDialogue == null) return

        lastDialogue!!.end.invoke()

        lastDialogue = null
        if (player.interfaceManager.containsChatBoxInter()) {
            player.interfaceManager.closeChatBoxInterface()
        }
    }

}