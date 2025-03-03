import com.hyze.commands.PlayerRight
import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin
import com.rs.game.world.entity.updating.impl.Animation

/*
* UnityScape Plugin Script
* @author Jovic
* @date 08/01/2025  
*/
val rightPredicate = predicate<CommandPlugin> {
    successIf { player.rights >= PlayerRight.ADMIN.rightId }
    onFailure { player.message("Você não tem permissão para usar esse comando", red = true) }
}

on<CommandPlugin>(
    identifiers = arrayOf("anim"),
    predicates = arrayOf(rightPredicate)
) {
    val id = arguments[0].toInt()
    player.setNextAnimationNoPriority(Animation(id))
    player.message("Performando animação para o seu personagem")
}