package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase

class UseCaseImpressionsTracker<ItemDescriptorT>(
    private val itemBecameVisibleUseCase: ItemBecameVisibleUseCase<ItemDescriptorT>,
    private val itemBecameNotVisibleUseCase: ItemBecameNotVisibleUseCase<ItemDescriptorT>
) : ImpressionsTracker<ItemDescriptorT> {

    override fun onItemBecameVisible(itemDescriptor: ItemDescriptorT) {
        itemBecameVisibleUseCase.execute(itemDescriptor)
    }

    override fun onItemBecameNotVisible(itemDescriptor: ItemDescriptorT) {
       itemBecameNotVisibleUseCase.execute(itemDescriptor)
    }
}