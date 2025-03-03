import com.hyze.extensions.message
import com.hyze.plugins.event.CommandPlugin

/*
* UnityScape Plugin Script
* @author Jovic
* @date 05/01/2025  
*/
on<CommandPlugin>(
    arrayOf("plugin")
) {
    when (arguments[0]) {
        "load" -> {
            player.packets.sendRunScript(
                109,
                "Digite o nome do plugin que você deseja carregar"
            )
            player.temporaryAttributtes["pluginload"] = true
        }
        else -> player.message("Este sub-comando não foi encontrado", red = true)
    }
}