package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.domain.usecases.ImpressionTimeElapsedUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameNotVisibleUseCase
import net.rossharper.impressionsexperiment.impressions.domain.usecases.ItemBecameVisibleUseCase
import java.util.*

const val IMPRESSION_DURATION_THRESHOLD: Long = 1000

fun <ItemDescriptorT> createImpressionsTracker(
    impressionObserver: ImpressionObserver <ItemDescriptorT>)
        : ImpressionsTracker<ItemDescriptorT> {

    val impressionsModel = ImpressionsModel<ItemDescriptorT>()
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

    return UseCaseImpressionsTracker(itemBecameVisibleUseCase, itemBecameNotVisibleUseCase)
}