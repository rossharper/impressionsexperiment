package net.rossharper.impressionsexperiment.impressions.domain.usecases

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel
import net.rossharper.impressionsexperiment.impressions.domain.TimestampProvider

class ItemBecameVisibleUseCase<ItemDescriptorT>(
    private val model: ImpressionsModel<ItemDescriptorT>,
    private val impressionTimeElapsedUseCase: ImpressionTimeElapsedUseCase<ItemDescriptorT>,
    private val timestampProvider: TimestampProvider,
    private val impressionDurationThresholdMillis : Long
) {
    fun execute(itemDescriptor: ItemDescriptorT) {
        // TODO: thread safety - synchronise adding/removing/reading from maps
        Log.v("IMPRESSIONS", "Item $itemDescriptor became visible")
        if (impressionNotAlreadySent(itemDescriptor)) {
            Log.v("IMPRESSIONS", "Item $itemDescriptor not impressed yet")
            addVisibleItem(itemDescriptor)
            waitForImpressions()
        }
    }

    private fun impressionNotAlreadySent(itemDescriptor: ItemDescriptorT) =
        !model.impressionsSent.contains(itemDescriptor)

    private fun addVisibleItem(itemDescriptor: ItemDescriptorT) {
        model.visiblePositionsByTimestamp[itemDescriptor] = timestampProvider.timeInMillis
    }

    private fun waitForImpressions() {
        if (!model.timerActive) {
            model.timerActive = true
            GlobalScope.launch {
                delay(impressionDurationThresholdMillis)
                impressionTimeElapsedUseCase.execute()
            }
            Log.v("IMPRESSIONS", "Timer started")
        }
    }
}