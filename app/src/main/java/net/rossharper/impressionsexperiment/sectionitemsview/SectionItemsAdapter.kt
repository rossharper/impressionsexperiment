package net.rossharper.impressionsexperiment.sectionitemsview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*
import net.rossharper.impressionsexperiment.Item
import net.rossharper.impressionsexperiment.R
import net.rossharper.impressionsexperiment.impressions.ui.HalfVisibleItemVisibilityStrategy
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityTrackingAdapter

class SectionItemsAdapter(private val data: List<Item>) : ItemVisibilityTrackingAdapter<SectionItemsAdapter.ViewHolder>(
    HalfVisibleItemVisibilityStrategy()
) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Item) {
            itemView.label.text = item.num.toString()

            if (item.num % 2 == 0)
                itemView.setBackgroundColor(Color.RED)
            else
                itemView.setBackgroundColor(Color.BLUE)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = 128

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}