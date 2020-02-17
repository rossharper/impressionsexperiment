package net.rossharper.impressionsexperiment.impressions.domain.usecases

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel
import net.rossharper.impressionsexperiment.impressions.domain.Timestamp
import java.util.*

class ImpressionTimeElapsedUseCase(private val model: ImpressionsModel) {
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
                if (hasBeenVisibleForLongEnough(timestamp)) { // TODO: extract constant, time provider
                    iterator.remove()
                    sendImpression(position)
                    model.impressionsSent.add(position)
                }
            }
        }
    }

    private fun hasBeenVisibleForLongEnough(timestamp: Timestamp) =
        Calendar.getInstance().timeInMillis - timestamp > 1000

    private fun sendImpression(position: Position) {
        Log.i("IMPRESSIONS", "Impression for position $position")
    }

    private fun waitForImpressions() {
        Log.i("IMPRESSIONS", "Delaying another second...")
        GlobalScope.launch {
            delay(1000) // TODO: extract constant
            execute()
        }
    }

    private fun stopWaitingForImpressions() {
        Log.i("IMPRESSIONS", "Impressions complete")
        model.timerActive = false
    }
}