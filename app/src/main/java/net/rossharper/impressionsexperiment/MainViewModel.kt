package net.rossharper.impressionsexperiment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionObserver
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsTracker
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver
import net.rossharper.impressionsexperiment.sectionsview.SectionItemDescriptor

class MainViewModel(
    private val telemetryGateway: TelemetryGateway,
    private val impressionsTracker: ImpressionsTracker<SectionItemDescriptor>
) : ViewModel(), ItemVisibilityObserver<SectionItemDescriptor> {

    private val _viewState = MutableLiveData<PresentationModel>()

    val viewState: LiveData<PresentationModel>
        get() = _viewState

    init {
        impressionsTracker.observer = object : ImpressionObserver<SectionItemDescriptor> {
            override fun onImpression(itemDescriptor: SectionItemDescriptor) {
                // The below would be in a use case under clean arch rather than just here in the view model
                val section = viewState.value!!.sections[itemDescriptor.sectionIndex]
                telemetryGateway.sendImpression(
                    section.id,
                    section.items[itemDescriptor.itemIndex].id
                )
            }
        }
    }

    fun onViewDisplayed() {
        CoroutineScope(Dispatchers.Default).launch {
            load()
        }
    }

    private fun load() {
        // A load use case would usually exist
        _viewState.postValue(createPresentationModel())
    }

    override fun onItemBecameVisible(itemDescriptor: SectionItemDescriptor) {
        impressionsTracker.onItemBecameVisible(itemDescriptor)
    }

    override fun onItemBecameNotVisible(itemDescriptor: SectionItemDescriptor) {
        impressionsTracker.onItemBecameNotVisible(itemDescriptor)
    }
}

