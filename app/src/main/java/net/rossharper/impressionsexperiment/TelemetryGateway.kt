package net.rossharper.impressionsexperiment

import android.util.Log

interface TelemetryGateway {
    fun sendImpression(sectionId: String, itemId: String)
}

class LoggingTelemetryGateway : TelemetryGateway {
    override fun sendImpression(sectionId: String, itemId: String) {
        Log.i("IMPRESSIONS", "Send impression for section $sectionId item $itemId")
    }
}