package net.rossharper.impressionsexperiment

import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import net.rossharper.impressionsexperiment.impressions.ui.HalfVisibleImpressionVisibilityStrategy
import net.rossharper.impressionsexperiment.impressions.ui.ImpressionVisibilityObserver
import net.rossharper.impressionsexperiment.impressions.ui.ImpressionsVisibilityAdapter
import net.rossharper.impressionsexperiment.impressions.domain.createImpressionsUseCases

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uc =
            createImpressionsUseCases()

        list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MyVisibilityAdapter().apply {
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

class MyVisibilityAdapter : ImpressionsVisibilityAdapter<MyVisibilityAdapter.ViewHolder>(
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


