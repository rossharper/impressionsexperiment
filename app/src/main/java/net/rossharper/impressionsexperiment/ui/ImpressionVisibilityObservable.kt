package net.rossharper.impressionsexperiment.impressions.ui

import net.rossharper.impressionsexperiment.Position

interface ImpressionVisibilityObserver {
    fun onItemBecameVisible(position: Position)
    fun onItemBecameNotVisible(position: Position)
}

interface ItemImpressionVisibilityObservable {
    var impressionVisibilityObserver: ImpressionVisibilityObserver?
}