import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin
import com.rs.game.discord.RichPresenceType

/*
* UnityScape Plugin Script
* @author Jovic
* @date 15/02/2025  
*/
val rightPredicate = predicate<CommandPlugin> {
    successIf { player.isOwner }
    onFailure { player.message("Você não pode executar esse comando", red = true) }
}

on<CommandPlugin>(
    identifiers = arrayOf("presence"),
    predicates = arrayOf(rightPredicate)
) {
    player.packets.sendPresenceUpdate(RichPresenceType.STATE, arguments[0])
    player.message("A atualização de presença foi atualizada! Cheque o seu Discord.")
}