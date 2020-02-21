package net.rossharper.impressionsexperiment.impressions.domain

interface ImpressionObserver<ItemDescriptorT> {
    fun onImpression(itemDescriptor: ItemDescriptorT)
}
