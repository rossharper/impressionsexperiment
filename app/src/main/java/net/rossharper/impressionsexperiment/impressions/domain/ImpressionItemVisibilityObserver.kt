package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver

class ImpressionItemVisibilityObserver(
    private val itemBecameVisibleUseCase: ItemBecameVisibleUseCase,
    private val itemBecameNotVisibleUseCase: ItemBecameNotVisibleUseCase
) : ItemVisibilityObserver {
    override fun onItemBecameVisible(position: Position) {
        itemBecameVisibleUseCase.execute(position)
    }

    override fun onItemBecameNotVisible(position: Position) {
        itemBecameNotVisibleUseCase.execute(position)
    }
}