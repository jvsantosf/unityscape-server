package com.hyze.event

interface Listener<out T : Event> {

    fun handle(event: @UnsafeVariance T)

}
