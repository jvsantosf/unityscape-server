import com.hyze.extensions.message
import com.hyze.plugins.event.ObjectPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 04/02/2025  
*/
private val FISH_ITEMS = arrayOf(379, 385, 373, 7946, 391, 1963, 315, 319, 325, 347, 361, 365, 333, 329, 351, 339)

val depositPredicate = predicate<ObjectPlugin> {
    successIf { player.inventory.containsAny(379, 385, 373, 7946, 391, 1963, 315, 319, 325, 347, 361, 365, 333, 329, 351, 339) }
    onFailure {
        player.message("Você não tem nenhum peixe para depositar aqui", red = true)
    }
}

on<ObjectPlugin>(
    identifiers = arrayOf(227554),
    options = arrayOf("Deposit"),
    predicates = arrayOf(depositPredicate)
) {
    player.message("Você guardou todos os seus peixes no seu banco")
}