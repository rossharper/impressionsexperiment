package net.rossharper.impressionsexperiment.sectionsview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import net.rossharper.impressionsexperiment.R
import net.rossharper.impressionsexperiment.Section
import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver
import net.rossharper.impressionsexperiment.sectionitemsview.SectionItemsAdapter

class SectionsAdapter : RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    var data: List<Section> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemVisibilityObserver: ItemVisibilityObserver<SectionItemDescriptor>? = null

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(section: Section) {
            itemView.list.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    RecyclerView.HORIZONTAL,
                    false
                )

                adapter = SectionItemsAdapter(section.items)
                    .apply {
                    itemVisibilityObserver = object :
                        ItemVisibilityObserver<Position> {
                        override fun onItemBecameVisible(itemDescriptor: Position) {
                            this@SectionsAdapter.itemVisibilityObserver?.onItemBecameVisible(
                                SectionItemDescriptor(
                                    adapterPosition,
                                    itemDescriptor
                                )
                            )
                        }

                        override fun onItemBecameNotVisible(itemDescriptor: Position) {
                            this@SectionsAdapter.itemVisibilityObserver?.onItemBecameNotVisible(
                                SectionItemDescriptor(
                                    adapterPosition,
                                    itemDescriptor
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.section,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}