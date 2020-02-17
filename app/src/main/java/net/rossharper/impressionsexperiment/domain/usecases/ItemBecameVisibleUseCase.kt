package net.rossharper.impressionsexperiment.impressions.domain.usecases

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.rossharper.impressionsexperiment.Position
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel
import java.util.*

class ItemBecameVisibleUseCase(private val model: ImpressionsModel, private val impressionTimeElapsedUseCase: ImpressionTimeElapsedUseCase) {
    fun execute(position: Position) {
        // TODO: thread safety - synchronise adding/removing/reading from maps
        Log.i("IMPRESSIONS", "Position ${position} became visible")
        if (!model.positionsAlreadyImpressed.contains(position)) {
            Log.i("IMPRESSIONS", "Position ${position} not impressed yet")
            addVisiblePosition(position)
            waitForImpressions()
        }
    }

    private fun addVisiblePosition(position: Position) {
        model.visiblePositionsByTimestamp[position] = Calendar.getInstance().timeInMillis // TODO: extract time provision
    }

    private fun waitForImpressions() {
        if (!model.timerActive) {
            model.timerActive = true
            GlobalScope.launch {
                delay(1000) // TODO: extract constant
                impressionTimeElapsedUseCase.execute()
            }
            Log.i("IMPRESSIONS", "Timer started")
        }
    }
}