package net.rossharper.impressionsexperiment.impressions.ui

import android.view.View

interface ImpressionVisibilityStrategy {
    fun isVisible(view: View): Boolean
}