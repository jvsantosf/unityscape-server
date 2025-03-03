package com.hyze.tools

import com.displee.cache.CacheLibrary
import com.displee.cache.ProgressListener
import com.rs.Constants

object RS2CacheTransfer {

    const val CACHE_FROM = "G:\\[Cache] RSPS\\718.901\\cache"
    const val CACHE_TO = Constants.CACHE_PATH

    @JvmStatic
    fun main(args: Array<String>) {
        val from = CacheLibrary.create(CACHE_TO)
        val to = CacheLibrary.create(CACHE_FROM)

        val index = to.index(14)
        from.index(14).cache()
        index.clear()
        index.add(*from.index(14).archives())
        index.update(TransferProgressListener())
    }

}

class TransferProgressListener : ProgressListener {

    override fun notify(progress: Double, message: String?) {
        println("$message -> ${"%.2f".format(progress * 100)}%")
    }

}