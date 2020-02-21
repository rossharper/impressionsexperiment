package net.rossharper.impressionsexperiment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.rossharper.impressionsexperiment.impressions.domain.createImpressionsTracker

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(LoggingTelemetryGateway(), createImpressionsTracker())
            }
            else -> {
                throw Exception("model not found")
            }
        } as T
    }

}
