package com.example.covid_19_livetracker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import com.example.covid_19_livetracker.TestActivity
import com.example.covid_19_livetracker.adapter.HeaderAdapter
import com.example.covid_19_livetracker.adapter.ItemAdapter
import com.example.covid_19_livetracker.databinding.ActivityMainBinding
import com.example.covid_19_livetracker.model.Details
import com.example.covid_19_livetracker.viewmodel.ListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class ListPage : AppCompatActivity() {
    private lateinit var viewModel: ListViewModel
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemAdapter: ItemAdapter
    private val backSnackBar by lazy {
        Snackbar.make(binding.root, "Press back again to exit", Snackbar.LENGTH_SHORT)
    }
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerAdapter = HeaderAdapter()
        itemAdapter = ItemAdapter(this::goToDetailPage)

        val adapter = MergeAdapter(headerAdapter, itemAdapter)
        recycler.adapter = adapter
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        with(viewModel) {
            stateResponse.observe(this@ListPage, Observer {
                headerAdapter.submitList(it.stateWiseDetails.subList(0, 1))
                itemAdapter.submitList(it.stateWiseDetails.subList(1, it.stateWiseDetails.size))

            })
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backSnackBar.dismiss()
            super.onBackPressed()
            return
        } else {
            backSnackBar.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun goToDetailPage(details: Details) {
        startActivity(Intent(this, TestActivity::class.java).apply {
            putExtra(
                KEY_STATE_DETAILS,
                details
            )
        })
    }

    companion object {
        const val KEY_STATE_DETAILS = "KEY_STATE_DETAILS"
    }

}