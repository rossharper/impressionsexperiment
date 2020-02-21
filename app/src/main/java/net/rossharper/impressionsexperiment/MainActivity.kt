package net.rossharper.impressionsexperiment

import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.section.view.*
import net.rossharper.impressionsexperiment.impressions.Position
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionObserver
import net.rossharper.impressionsexperiment.impressions.domain.createImpressionsTracker
import net.rossharper.impressionsexperiment.impressions.ui.HalfVisibleItemVisibilityStrategy
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityTrackingAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val impressionObserver = object : ImpressionObserver<SectionItemDescriptor> {
            override fun onImpression(itemDescriptor: SectionItemDescriptor) {
                Log.i(
                    "IMPRESSIONS",
                    "Impression at Section ${itemDescriptor.sectionIndex}, Item ${itemDescriptor.itemIndex}"
                )
            }
        }

        list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MySectionsAdapter(createImpressionsTracker(impressionObserver))
        }
    }
}

data class SectionItemDescriptor(val sectionIndex: Int, val itemIndex: Int)

class MySectionsAdapter(private val itemVisibilityObserver: ItemVisibilityObserver<SectionItemDescriptor>) :
    RecyclerView.Adapter<MySectionsAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.list.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    RecyclerView.HORIZONTAL,
                    false
                )

                adapter = MySectionItemsAdapter().apply {
                    itemVisibilityObserver = object : ItemVisibilityObserver<Position> {
                        override fun onItemBecameVisible(itemDescriptor: Position) {
                            this@MySectionsAdapter.itemVisibilityObserver.onItemBecameVisible(
                                SectionItemDescriptor(adapterPosition, itemDescriptor)
                            )
                        }

                        override fun onItemBecameNotVisible(itemDescriptor: Position) {
                            this@MySectionsAdapter.itemVisibilityObserver.onItemBecameNotVisible(
                                SectionItemDescriptor(adapterPosition, itemDescriptor)
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

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}

class MySectionItemsAdapter : ItemVisibilityTrackingAdapter<MySectionItemsAdapter.ViewHolder>(
    HalfVisibleItemVisibilityStrategy()
) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(number: Int) {
            itemView.label.text = number.toString()

            if (number % 2 == 0)
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
        holder.bind(position)
    }
}


