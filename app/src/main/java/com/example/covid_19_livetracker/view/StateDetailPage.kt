package com.example.covid_19_livetracker.view

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.covid_19_livetracker.databinding.ActivityStateDetailPageBinding
import com.example.covid_19_livetracker.model.Details
import com.example.covid_19_livetracker.viewmodel.StateDetailPageViewModel

class StateDetailPage : AppCompatActivity() {
    private lateinit var viewModel : StateDetailPageViewModel
    private lateinit var binding : ActivityStateDetailPageBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.e("StateDetailPage","StateDetailPageStateDetailPageStateDetailPageStateDetailPageStateDetailPageStateDetailPageStateDetailPage")
        binding = ActivityStateDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(StateDetailPageViewModel::class.java)
        viewModel.getStateDetail(getStateDetailFromIntent()?.state)
    }

    private fun getStateDetailFromIntent(): Details? = intent.getParcelableExtra(ListPage.KEY_STATE_DETAILS)

}