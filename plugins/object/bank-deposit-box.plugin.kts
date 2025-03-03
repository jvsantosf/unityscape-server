import com.hyze.extensions.message
import com.hyze.plugins.event.ObjectPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 04/01/2025  
*/
val inventoryFullPredicate = predicate<ObjectPlugin> {
    successIf { player.inventory.freeSlots > 0 }
    onFailure { player.message("Você não tem itens para depositar ao banco.", red = true) }
}

on<ObjectPlugin>(
    identifiers = arrayOf("Pulley lift"),
    options = arrayOf("Deposit"),
    predicates = arrayOf(inventoryFullPredicate)
) {
    player.bank.openDepositBox()
}