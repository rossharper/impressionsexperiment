package net.rossharper.impressionsexperiment.impressions.ui

import androidx.recyclerview.widget.RecyclerView
import net.rossharper.impressionsexperiment.Position

abstract class ImpressionsVisibilityAdapter<ViewHolderT : RecyclerView.ViewHolder>(private val impressionVisibilityStrategy: ImpressionVisibilityStrategy) : RecyclerView.Adapter<ViewHolderT>(),
    ItemImpressionVisibilityObservable {

    private val attachedViews = HashSet<ViewHolderT>()
    private val visiblePositions = HashSet<Position>()

    override var impressionVisibilityObserver: ImpressionVisibilityObserver? = null

    override fun onViewAttachedToWindow(holder: ViewHolderT) {
        super.onViewAttachedToWindow(holder)

        attachedViews.add(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderT) {
        super.onViewDetachedFromWindow(holder)

        attachedViews.remove(holder)
        if(visiblePositions.contains(holder.adapterPosition)) {
            visiblePositions.remove(holder.adapterPosition)
            impressionVisibilityObserver?.onItemBecameNotVisible(holder.adapterPosition)
        }
    }

    private val onDrawListener = {
        detectNewImpressions()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerView.viewTreeObserver.addOnDrawListener(onDrawListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        recyclerView.viewTreeObserver.removeOnDrawListener(onDrawListener)
    }

    private fun detectNewImpressions() {
        attachedViews.forEach { viewHolder ->

            if (impressionVisibilityStrategy.isVisible(viewHolder.itemView)) {
                if (!visiblePositions.contains(viewHolder.adapterPosition)) {
                    visiblePositions.add(viewHolder.adapterPosition)
                    impressionVisibilityObserver?.onItemBecameVisible(viewHolder.adapterPosition)
                }
            } else {
                if (visiblePositions.contains(viewHolder.adapterPosition)) {
                    visiblePositions.remove(viewHolder.adapterPosition)
                    impressionVisibilityObserver?.onItemBecameNotVisible(viewHolder.adapterPosition)
                }
            }
        }
    }
}

