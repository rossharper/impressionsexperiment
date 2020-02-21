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
import net.rossharper.impressionsexperiment.impressions.domain.ImpressionsTracker
import net.rossharper.impressionsexperiment.impressions.domain.createImpressionsTracker
import net.rossharper.impressionsexperiment.impressions.ui.HalfVisibleItemVisibilityStrategy
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityObserver
import net.rossharper.impressionsexperiment.impressions.ui.ItemVisibilityTrackingAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MySectionsAdapter()
        }
    }
}

class MySectionsAdapter : RecyclerView.Adapter<MySectionsAdapter.ViewHolder>() {

    data class SectionItemDescriptor(val sectionIndex: Int, val itemIndex: Int)

    private val impressionObserver = object : ImpressionObserver<SectionItemDescriptor> {
        override fun onImpression(itemDescriptor: SectionItemDescriptor) {
            Log.i(
                "IMPRESSIONS",
                "Impression at Section ${itemDescriptor.sectionIndex}, Item ${itemDescriptor.itemIndex}"
            )
        }
    }

    private val impressionsTracker = createImpressionsTracker(impressionObserver)

    class ViewHolder(
        itemView: View,
        private val impressionsTracker: ImpressionsTracker<SectionItemDescriptor>
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.list.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    RecyclerView.HORIZONTAL,
                    false
                )

                adapter = MySectionItemsAdapter().apply {
                    itemVisibilityObserver = object : ItemVisibilityObserver {
                        override fun onItemBecameVisible(position: Position) {
                            this@ViewHolder.impressionsTracker.onItemBecameVisible(
                                SectionItemDescriptor(adapterPosition, position)
                            )
                        }

                        override fun onItemBecameNotVisible(position: Position) {
                            this@ViewHolder.impressionsTracker.onItemBecameNotVisible(
                                SectionItemDescriptor(adapterPosition, position)
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
            ),
            impressionsTracker
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


