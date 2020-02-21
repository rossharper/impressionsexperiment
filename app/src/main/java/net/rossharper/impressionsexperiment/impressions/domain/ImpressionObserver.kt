package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.Position

interface ImpressionObserver {
    fun onImpression(position: Position)
}
