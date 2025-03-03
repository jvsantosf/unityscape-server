package com.hyze.plugins.dialogue.message

import com.hyze.plugins.dialogue.DialogueType
import com.hyze.plugins.dialogue.Message
import com.rs.game.world.entity.player.Player
import java.util.*

class OptionMessageDialogue(optionTitle: String, action: ArrayList<() -> Unit>?) : Message(
    DialogueType.OPTIONS, -1, -1, -1, -1, optionTitle, arrayListOf(), action
) {

    private val OPTION_INTERFACE = 1188

    override fun display(player: Player) {
        player.interfaceManager.sendChatBoxInterface(OPTION_INTERFACE)

        var i = 0
        val params = arrayOfNulls<Any>(message.size + 1)
        params[i++] = message.size
        val optionsList = message.toList().asReversed()
        for (string in optionsList) {
            params[i++] = string
        }

        player.packets.sendRunScript(5589, *params)
        player.packets.sendIComponentText(OPTION_INTERFACE, 20, title)
    }

    enum class Option(val componentId: Int) {
        ONE(11), TWO(13), THREE(14), FOUR(15), FIVE(16);

        companion object {

            @JvmStatic
            fun getOption(componentId: Int): Option {
                for (option in entries) {
                    if (option.componentId == componentId) {
                        return option
                    }
                }
                return ONE
            }
        }

    }
}