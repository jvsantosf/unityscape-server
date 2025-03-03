package com.hyze.extensions

import com.rs.cores.CoresManager
import com.rs.cores.tasks.WorldTask
import com.rs.cores.tasks.WorldTasksManager
import java.util.TimerTask

inline fun task(delay: Long, crossinline block: () -> Unit) {
    CoresManager.fastExecutor.schedule(object : TimerTask() {
        override fun run() {
            block.invoke()
        }
    }, delay)
}

inline fun schedule(delay: Int, crossinline block: () -> Unit) {
    WorldTasksManager.schedule(object : WorldTask() {
        override fun run() {
            block.invoke()
        }
    }, delay)
}