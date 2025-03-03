import com.hyze.extensions.message
import com.hyze.plugins.event.ObjectPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 16/02/2025  
*/
val levelPredicate = predicate<ObjectPlugin> {
    successIf { player.completedCooksAssistantQuest && player.isCompletedFightCaves && player.isCompletedFightKiln }
    onFailure { player.message("Você não tem todos os requisitos necessários para acessar Priffidinas, fale com Wise para saber mais", red = true) }
}

on<ObjectPlugin>(
    identifiers = arrayOf("Gauntlet Portal"),
    options = arrayOf("Enter"),
    predicates = arrayOf(levelPredicate)
) {
    player.message("Você está sendo levado até Priffidinas")
}