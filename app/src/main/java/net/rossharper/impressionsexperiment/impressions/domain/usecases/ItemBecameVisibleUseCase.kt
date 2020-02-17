package net.rossharper.impressionsexperiment.impressions.domain.usecases

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel
import net.rossharper.impressionsexperiment.impressions.domain.TimestampProvider
import java.util.*

class ItemBecameVisibleUseCase(
    private val model: ImpressionsModel,
    private val impressionTimeElapsedUseCase: ImpressionTimeElapsedUseCase,
    private val timestampProvider: TimestampProvider,
    private val impressionDurationThresholdMillis : Long
) {
    fun execute(position: Position) {
        // TODO: thread safety - synchronise adding/removing/reading from maps
        Log.i("IMPRESSIONS", "Position ${position} became visible")
        if (impressionNotAlreadySent(position)) {
            Log.i("IMPRESSIONS", "Position ${position} not impressed yet")
            addVisiblePosition(position)
            waitForImpressions()
        }
    }

    private fun impressionNotAlreadySent(position: Position) =
        !model.impressionsSent.contains(position)

    private fun addVisiblePosition(position: Position) {
        model.visiblePositionsByTimestamp[position] = timestampProvider.timeInMillis
    }

    private fun waitForImpressions() {
        if (!model.timerActive) {
            model.timerActive = true
            GlobalScope.launch {
                delay(impressionDurationThresholdMillis)
                impressionTimeElapsedUseCase.execute()
            }
            Log.i("IMPRESSIONS", "Timer started")
        }
    }
}