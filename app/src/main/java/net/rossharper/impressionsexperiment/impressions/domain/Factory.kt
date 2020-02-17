package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.domain.usecases.ImpressionTimeElapsedUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase
import java.util.*

const val IMPRESSION_DURATION_THRESHOLD: Long = 1000

fun createImpressionsUseCases(): ImpressionsUseCases {
    val impressionsModel = ImpressionsModel()
    val timestampProvider = object : TimestampProvider {
        override val timeInMillis: Long
            get() = Calendar.getInstance().timeInMillis
    }
    val impressionTimeElapsedUseCase = ImpressionTimeElapsedUseCase(
        impressionsModel,
        timestampProvider,
        IMPRESSION_DURATION_THRESHOLD
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