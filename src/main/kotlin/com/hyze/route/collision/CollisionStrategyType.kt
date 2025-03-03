package com.hyze.route.collision

enum class CollisionStrategyType(val strategy: CollisionStrategy) {
    NORMAL(NormalBlockFlagCollision()),
    WATER(BlockedFlagCollision()),
    FLY(LineOfSightBlockFlagCollision()),
    INDOOR(IndoorsFlagCollision()),
    OUTDOOR(OutdoorsFlagCollision())
}
