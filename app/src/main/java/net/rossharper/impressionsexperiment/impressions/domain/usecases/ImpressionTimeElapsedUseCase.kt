package net.rossharper.impressionsexperiment.impressions.domain.usecases

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionObserver
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel
import net.rossharper.impressionsexperiment.impressions.domain.Timestamp
import net.rossharper.impressionsexperiment.impressions.domain.TimestampProvider

class ImpressionTimeElapsedUseCase(
    private val model: ImpressionsModel,
    private val timestampProvider: TimestampProvider,
    private val impressionDurationThresholdMillis: Long,
    private val impressionObserver: ImpressionObserver
) {
    fun execute() {
        sendImpressions()

        if (model.visiblePositionsByTimestamp.isNotEmpty()) {
            waitForImpressions()
        } else {
            stopWaitingForImpressions()
        }
    }

    private fun sendImpressions() {
        val iterator = model.visiblePositionsByTimestamp.iterator()
        while (iterator.hasNext()) {
            iterator.next().let {
                val (position, timestamp) = it
                if (hasBeenVisibleForLongEnough(timestamp)) {
                    iterator.remove()
                    sendImpression(position)
                    model.impressionsSent.add(position)
                }
            }
        }
    }

    private fun hasBeenVisibleForLongEnough(timestamp: Timestamp) =
        elapsedSince(timestamp) > impressionDurationThresholdMillis

    private fun elapsedSince(timestamp: Timestamp): Long {
        return timestampProvider.timeInMillis - timestamp
    }

    private fun sendImpression(position: Position) {
        impressionObserver.onImpression(position)
    }

    private fun waitForImpressions() {
        Log.v("IMPRESSIONS", "Delaying another second...")
        GlobalScope.launch {
            delay(impressionDurationThresholdMillis)
            execute()
        }
    }

    private fun stopWaitingForImpressions() {
        Log.v("IMPRESSIONS", "Impressions complete")
        model.timerActive = false
    }
}