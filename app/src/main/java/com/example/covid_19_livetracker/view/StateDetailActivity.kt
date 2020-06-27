package com.example.covid_19_livetracker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import com.example.covid_19_livetracker.R
import com.example.covid_19_livetracker.adapter.DistrictsAdapter
import com.example.covid_19_livetracker.adapter.HeaderAdapter
import com.example.covid_19_livetracker.databinding.ActivityStateDetailPageBinding
import com.example.covid_19_livetracker.model.Details
import com.example.covid_19_livetracker.utils.getPeriod
import com.example.covid_19_livetracker.utils.toDateFormat
import com.example.covid_19_livetracker.view.ListPage.Companion.KEY_STATE_DETAILS
import com.example.covid_19_livetracker.viewmodel.StateDetailPageViewModel

class StateDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityStateDetailPageBinding
    lateinit var viewModel: StateDetailPageViewModel
    private val headerAdapter = HeaderAdapter()
    private val districtAdapter = DistrictsAdapter()
    private val adapter = MergeAdapter(headerAdapter, districtAdapter)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(StateDetailPageViewModel::class.java)
        viewModel.getStateDetail(getStateDetails()?.state)
        initView()
        onbserveData()
    }

    private fun onbserveData() {
        with(viewModel) {
            stateResponse.observe(this@StateDetailActivity, Observer {
                val list = it.districtData
                list.sortedByDescending { it.confirmed }.let {
                    districtAdapter.submitList(it)
                }
            })
            loading.observe(this@StateDetailActivity, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
        }
    }

    private fun initView() {
        setSupportActionBar(binding.appBarlayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerState.adapter = adapter
        binding.appBarlayout.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val details: Details? = getStateDetails()

        details?.let {
            headerAdapter.submitList(listOf(it))

            supportActionBar?.title = it.state
            supportActionBar?.subtitle = getString(
                R.string.text_last_updated,
                getPeriod(
                    it.lastUpdatedTime.toDateFormat()
                )
            )
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getStateDetail(getStateDetails()?.state)
        }
    }

    private fun getStateDetails(): Details? = intent.getParcelableExtra(KEY_STATE_DETAILS)
}