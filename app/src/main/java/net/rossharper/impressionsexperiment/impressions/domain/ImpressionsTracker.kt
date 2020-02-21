package net.rossharper.impressionsexperiment.impressions.domain

import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver

interface ImpressionsTracker<ItemDescriptorT> : ItemVisibilityObserver<ItemDescriptorT>
