package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.domain.usecases.ImpressionTimeElapsedUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase

class UseCaseImpressionsTracker<ItemDescriptorT>(
    private val itemBecameVisibleUseCase: ItemBecameVisibleUseCase<ItemDescriptorT>,
    private val itemBecameNotVisibleUseCase: ItemBecameNotVisibleUseCase<ItemDescriptorT>,
    private val impressionTimeElapsedUseCase: ImpressionTimeElapsedUseCase<ItemDescriptorT>
) : ImpressionsTracker<ItemDescriptorT> {

    override var observer: ImpressionObserver<ItemDescriptorT>?
        get() = impressionTimeElapsedUseCase.observer
        set(value) {
            impressionTimeElapsedUseCase.observer = value
        }

    override fun onItemBecameVisible(itemDescriptor: ItemDescriptorT) {
        itemBecameVisibleUseCase.execute(itemDescriptor)
    }

    override fun onItemBecameNotVisible(itemDescriptor: ItemDescriptorT) {
       itemBecameNotVisibleUseCase.execute(itemDescriptor)
    }
}