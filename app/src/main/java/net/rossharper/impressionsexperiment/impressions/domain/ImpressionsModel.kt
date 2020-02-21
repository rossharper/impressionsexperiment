package net.rossharper.impressionsexperiment.impressions.domain

data class ImpressionsModel<ItemDescriptorT>(
    val impressionsSent: MutableSet<ItemDescriptorT> = mutableSetOf(),
    val visiblePositionsByTimestamp: MutableMap<ItemDescriptorT, Timestamp> = mutableMapOf()
) {
    @get:Synchronized @set:Synchronized
    var timerActive: Boolean = false
}