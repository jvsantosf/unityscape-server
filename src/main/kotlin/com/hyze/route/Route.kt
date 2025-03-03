@file:Suppress("MemberVisibilityCanBePrivate")

package com.hyze.route

data class Route(
    val coords: ArrayDeque<RouteCoordinates>,
    val alternative: Boolean,
    val success: Boolean
) : List<RouteCoordinates> by coords {

    val failed: Boolean
        get() = !success
}

@JvmInline
value class RouteCoordinates(val packed: Int) {

    val x: Int
        get() = packed and 0xFFFF

    val y: Int
        get() = (packed shr 16) and 0xFFFF

    constructor(x: Int, y: Int) : this(
        (x and 0xFFFF) or ((y and 0xFFFF) shl 16)
    )

    fun translate(xOffset: Int, yOffset: Int): RouteCoordinates {
        return RouteCoordinates(
            x = x + xOffset,
            y = y + yOffset
        )
    }

    fun translateX(offset: Int): RouteCoordinates = translate(offset, 0)

    fun translateY(offset: Int): RouteCoordinates = translate(0, offset)

    override fun toString(): String {
        return "${javaClass.simpleName}{x=$x, y=$y}"
    }
}

data class JavaRouteCoordinates(val x: Int, val y: Int) {

    companion object {

        @JvmStatic
        fun of(coords: RouteCoordinates): JavaRouteCoordinates {
            return JavaRouteCoordinates(coords.x, coords.y)
        }

        @JvmStatic
        fun of(coords: List<RouteCoordinates>): List<JavaRouteCoordinates> {
            return coords.map { of(it) }
        }

        /**
         * RouteCoordinates helper function to avoid memory-creation
         * with [JavaRouteCoordinates.of].
         */
        @JvmStatic
        fun getX(coords: RouteCoordinates): Int {
            return coords.x
        }

        /**
         * RouteCoordinates helper function to avoid memory-creation
         * with [JavaRouteCoordinates.of].
         */
        @JvmStatic
        fun getY(coords: RouteCoordinates): Int {
            return coords.y
        }

        /**
         * RouteCoordinates helper function to avoid memory-creation
         * with [JavaRouteCoordinates.of].
         */
        @JvmStatic
        fun translate(coords: RouteCoordinates, xOffset: Int, yOffset: Int): RouteCoordinates {
            return coords.translate(xOffset, yOffset)
        }

        /**
         * RouteCoordinates helper function to avoid memory-creation
         * with [JavaRouteCoordinates.of].
         */
        @JvmStatic
        fun translateX(coords: RouteCoordinates, offset: Int): RouteCoordinates {
            return coords.translateX(offset)
        }

        /**
         * RouteCoordinates helper function to avoid memory-creation
         * with [JavaRouteCoordinates.of].
         */
        @JvmStatic
        fun translateY(coords: RouteCoordinates, offset: Int): RouteCoordinates {
            return coords.translateY(offset)
        }
    }
}

