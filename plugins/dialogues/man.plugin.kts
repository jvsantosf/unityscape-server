import com.hyze.plugins.dialogue.DialogueExtension.startDialogue
import com.hyze.plugins.event.NPCPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 03/01/2025  
*/
on<NPCPlugin>(
    identifiers = arrayOf("Man"),
    options = arrayOf("Talk-to")
) {
    player.startDialogue(npc.id) {
        npc("Quem é você?!")
    }
}