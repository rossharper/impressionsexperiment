package net.rossharper.impressionsexperiment

import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LayoutDirection
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
import net.rossharper.impressionsexperiment.impressions.ui.HalfVisibleImpressionVisibilityStrategy
import net.rossharper.impressionsexperiment.impressions.ui.ImpressionVisibilityObserver
import net.rossharper.impressionsexperiment.impressions.ui.ImpressionsVisibilityAdapter
import net.rossharper.impressionsexperiment.impressions.domain.createImpressionsUseCases

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

            val uc =
                createImpressionsUseCases( object : ImpressionObserver {
                    override fun onImpression(position: Position) {
                        Log.i("IMPRESSION", "Impression for item $position in section $adapterPosition")
                    }
                })

            itemView.list.apply {
                layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
                adapter = MySectionItemsAdapter().apply {
                    impressionVisibilityObserver = object :
                        ImpressionVisibilityObserver {
                        override fun onItemBecameVisible(position: Position) {
                            uc.itemBecameVisibleUseCase.execute(position)
                        }

                        override fun onItemBecameNotVisible(position: Position) {
                            uc.itemBecameNotVisibleUseCase.execute(position)
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

class MySectionItemsAdapter : ImpressionsVisibilityAdapter<MySectionItemsAdapter.ViewHolder>(
    HalfVisibleImpressionVisibilityStrategy()
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


