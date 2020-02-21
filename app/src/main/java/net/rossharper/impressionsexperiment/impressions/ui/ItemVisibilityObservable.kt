package net.rossharper.impressionsexperiment.impressions.ui

import net.rossharper.impressionsexperiment.impressions.Position

interface ItemVisibilityObserver {
    fun onItemBecameVisible(position: Position)
    fun onItemBecameNotVisible(itemDescriptor: Position)
}

interface ItemVisibilityObservable {
    var itemVisibilityObserver: ItemVisibilityObserver?
}