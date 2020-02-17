package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.domain.usecases.ImpressionTimeElapsedUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase


fun createImpressionsUseCases(): ImpressionsUseCases {
    val impressionsModel =
        ImpressionsModel()
    val impressionTimeElapsedUseCase =
        ImpressionTimeElapsedUseCase(
            impressionsModel
        )
    val itemBecameVisibleUseCase =
        ItemBecameVisibleUseCase(
            impressionsModel,
            impressionTimeElapsedUseCase
        )
    val itemBecameNotVisibleUseCase =
        ItemBecameNotVisibleUseCase(
            impressionsModel
        )

    return ImpressionsUseCases(
        itemBecameVisibleUseCase,
        itemBecameNotVisibleUseCase
    )
}

data class ImpressionsUseCases(
    val itemBecameVisibleUseCase: ItemBecameVisibleUseCase,
    val itemBecameNotVisibleUseCase: ItemBecameNotVisibleUseCase
)