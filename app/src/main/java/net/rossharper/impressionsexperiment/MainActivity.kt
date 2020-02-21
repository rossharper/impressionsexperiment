package net.rossharper.impressionsexperiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import net.rossharper.impressionsexperiment.sectionsview.SectionsAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val sectionsAdapter = SectionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = sectionsAdapter
        }

        viewModel = ViewModelProvider(this, ViewModelFactory()).get(MainViewModel::class.java)
        sectionsAdapter.itemVisibilityObserver = viewModel
        viewModel.viewState.observe(this, Observer { presentationModel ->
            presentationModel?.let { display(it) }
        })
        viewModel.onViewDisplayed()

    }

    private fun display(presentationModel: PresentationModel) {
        sectionsAdapter.data = presentationModel.sections
    }
}


