package net.rossharper.impressionsexperiment.impressions.domain.usecases

import android.util.Log
import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsModel

class ItemBecameNotVisibleUseCase(private val model: ImpressionsModel) {
    fun execute(position: Position) {
        // TODO: thread safety - synchronise adding/removing from this map?
        Log.i("IMPRESSIONS", "Position $position became not visible")
        model.visiblePositionsByTimestamp.remove(position)
    }

}