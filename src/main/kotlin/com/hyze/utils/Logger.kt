package com.hyze.utils

object Logger {

    const val ANSI_RESET = "\u001B[0m"
    const val ANSI_BLACK = "\u001B[30m"
    const val ANSI_RED = "\u001B[31m"
    const val ANSI_GREEN = "\u001B[32m"
    const val ANSI_YELLOW = "\u001B[33m"
    const val ANSI_BLUE = "\u001B[34m"
    const val ANSI_PURPLE = "\u001B[35m"
    const val ANSI_CYAN = "\u001B[36m"
    const val ANSI_WHITE = "\u001B[37m"

    const val ANSI_BLACK_BACKGROUND = "\u001B[40m"
    const val ANSI_RED_BACKGROUND = "\u001B[41m"
    const val ANSI_GREEN_BACKGROUND = "\u001B[42m"
    const val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
    const val ANSI_BLUE_BACKGROUND = "\u001B[44m"
    const val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
    const val ANSI_CYAN_BACKGROUND = "\u001B[46m"
    const val ANSI_WHITE_BACKGROUND = "\u001B[47m"

    fun warn(message: String) {
        if (Settings.DEBUG)
            println(" $ANSI_YELLOW WARNING: $message$ANSI_RESET")
    }

    fun debug(message: String) {
        if (Settings.DEBUG)
            println(" $ANSI_BLUE DEBUGGING: $message$ANSI_RESET")
    }

    fun error(message: String) {
        if (Settings.DEBUG)
            println(" $ANSI_RED ERROR: $message$ANSI_RESET")
    }

}