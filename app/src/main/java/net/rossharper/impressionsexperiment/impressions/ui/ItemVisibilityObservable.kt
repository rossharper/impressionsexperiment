package net.rossharper.impressionsexperiment.impressions.ui

interface ItemVisibilityObserver<ItemDescriptorT> {
    fun onItemBecameVisible(itemDescriptor: ItemDescriptorT)
    fun onItemBecameNotVisible(itemDescriptor: ItemDescriptorT)
}

interface ItemVisibilityObservable<ItemDescriptorT> {
    var itemVisibilityObserver: ItemVisibilityObserver<ItemDescriptorT>?
}