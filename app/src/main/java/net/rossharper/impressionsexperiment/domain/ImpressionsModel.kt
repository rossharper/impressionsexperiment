package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.Position
import net.rossharper.impressionsexperiment.Timestamp

data class ImpressionsModel(
    val positionsAlreadyImpressed: MutableSet<Position> = mutableSetOf(),
    val visiblePositionsByTimestamp: MutableMap<Position, Timestamp> = mutableMapOf()
) {
    @get:Synchronized @set:Synchronized
    var timerActive: Boolean = false
}