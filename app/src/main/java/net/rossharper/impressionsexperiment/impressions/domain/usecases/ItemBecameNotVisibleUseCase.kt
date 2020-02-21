package net.rossharper.impressionsexperiment.impressions.domain.usecases

import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel

class ItemBecameNotVisibleUseCase<ItemDescriptorT>(
    private val model: ImpressionsModel<ItemDescriptorT>) {

    fun execute(itemDescriptor: ItemDescriptorT) {
        // TODO: thread safety - synchronise adding/removing from this map?
        model.visiblePositionsByTimestamp.remove(itemDescriptor)
    }

}