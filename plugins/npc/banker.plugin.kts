import com.hyze.plugins.dialogue.DialogueExtension.startDialogue
import com.hyze.plugins.event.NPCPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 30/12/2024  
*/
on<NPCPlugin>(
    identifiers = arrayOf("Banker", 3), // Banker, Man
    options = arrayOf("Talk-to", "Bank")
) {
    val id = npc.id

    if (option == "Bank") {
        player.bank.openBank()
        return@on
    }

    player.startDialogue(id) {
        if (player.isLendingItem) {
            npc("""
                Before we go any further, I'd like to remind you that you
                currently have an ${player.trade.lendedItem} on loan to. Would you like to claim it?
            """.trimIndent())
        }

        npc("Good day. How may I help you?")
        options("What would you like to say?") {
            option("I'd like to access my bank account, please.") {
                player.bank.openBank()
            }
            option("I'd like to check my PIN settings") {
                player.bank.openSetPin()
            }
            option("I'd like to see my collection box") {
                // TODO: open collection box
            }
            option("What is this place?") {
                npc("This is a branch of the Bank of Gielinor. We have branches in many towns.")
                options("What would you like to say") {
                    option("And what do you do?") {
                        npc("We will look after your items and money for you. Leave your valuables with us if you want to keep them safe.")
                    }
                    option("Didn't you used to be called the Bank of Varrock?") {
                        npc("Yes we did, but people kept on coming into our branches outside of Varrock and telling us that our signs were wrong. They acted as if we didn't know what town we were in or something.")
                    }
                }
            }
        }
    }
}