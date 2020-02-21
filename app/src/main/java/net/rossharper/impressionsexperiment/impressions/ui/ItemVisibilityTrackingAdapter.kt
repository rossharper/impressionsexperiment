package net.rossharper.impressionsexperiment.impressions.ui

import androidx.recyclerview.widget.RecyclerView
import net.rossharper.impressionsexperiment.impressions.Position

abstract class ItemVisibilityTrackingAdapter<ViewHolderT : RecyclerView.ViewHolder>(
    private val itemVisibilityStrategy: ItemVisibilityStrategy)
    : RecyclerView.Adapter<ViewHolderT>(),
    ItemVisibilityObservable {

    private val attachedViews = HashSet<ViewHolderT>()
    private val visiblePositions = HashSet<Position>()

    override var itemVisibilityObserver: ItemVisibilityObserver? = null

    override fun onViewAttachedToWindow(holder: ViewHolderT) {
        super.onViewAttachedToWindow(holder)

        attachedViews.add(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderT) {
        super.onViewDetachedFromWindow(holder)

        attachedViews.remove(holder)
        if(visiblePositions.contains(holder.adapterPosition)) {
            visiblePositions.remove(holder.adapterPosition)
            itemVisibilityObserver?.onItemBecameNotVisible(holder.adapterPosition)
        }
    }

    private val onDrawListener = {
        detectNewVisibleItems()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerView.viewTreeObserver.addOnDrawListener(onDrawListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        recyclerView.viewTreeObserver.removeOnDrawListener(onDrawListener)
    }

    private fun detectNewVisibleItems() {
        attachedViews.forEach { viewHolder ->

            if (itemVisibilityStrategy.isVisible(viewHolder.itemView)) {
                if (!visiblePositions.contains(viewHolder.adapterPosition)) {
                    visiblePositions.add(viewHolder.adapterPosition)
                    itemVisibilityObserver?.onItemBecameVisible(viewHolder.adapterPosition)
                }
            } else {
                if (visiblePositions.contains(viewHolder.adapterPosition)) {
                    visiblePositions.remove(viewHolder.adapterPosition)
                    itemVisibilityObserver?.onItemBecameNotVisible(viewHolder.adapterPosition)
                }
            }
        }
    }
}

