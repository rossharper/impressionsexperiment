package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.domain.usecases.ImpressionTimeElapsedUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver
import java.util.*

const val IMPRESSION_DURATION_THRESHOLD: Long = 1000

fun createImpressionItemVisibilityObserver(impressionObserver: ImpressionObserver): ItemVisibilityObserver {
    with(createImpressionsUseCases(impressionObserver)) {
        return ImpressionItemVisibilityObserver(
            itemBecameVisibleUseCase,
            itemBecameNotVisibleUseCase
        )
    }
}

private fun createImpressionsUseCases(impressionObserver: ImpressionObserver): ImpressionsUseCases {
    val impressionsModel = ImpressionsModel()
    val timestampProvider = object : TimestampProvider {
        override val timeInMillis: Long
            get() = Calendar.getInstance().timeInMillis
    }
    val impressionTimeElapsedUseCase = ImpressionTimeElapsedUseCase(
        impressionsModel,
        timestampProvider,
        IMPRESSION_DURATION_THRESHOLD,
        impressionObserver

    )
    val itemBecameVisibleUseCase =
        ItemBecameVisibleUseCase(
            impressionsModel,
            impressionTimeElapsedUseCase,
            timestampProvider,
            IMPRESSION_DURATION_THRESHOLD
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