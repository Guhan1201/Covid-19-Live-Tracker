package com.example.covid_19_livetracker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import androidx.work.*
import com.example.covid_19_livetracker.NotificationWorker
import com.example.covid_19_livetracker.adapter.HeaderAdapter
import com.example.covid_19_livetracker.adapter.ItemAdapter
import com.example.covid_19_livetracker.databinding.ActivityMainBinding
import com.example.covid_19_livetracker.model.Details
import com.example.covid_19_livetracker.viewmodel.ListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import com.example.covid_19_livetracker.R
import com.example.covid_19_livetracker.utils.applyTheme
import com.example.covid_19_livetracker.utils.isDarkTheme

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
        initWorker()
        initDarkTheme()
        setSupportActionBar(binding.appBarlayout.toolbar)

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
            loading.observe(this@ListPage, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getData()
        }
    }

    private fun initDarkTheme() {
        val sharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        sharedPreferences.getInt("haha", AppCompatDelegate.MODE_NIGHT_NO).apply {
            applyTheme(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
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
        startActivity(Intent(this, StateDetailActivity::class.java).apply {
            putExtra(
                KEY_STATE_DETAILS,
                details
            )
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return when (item.itemId) {
            R.id.menu_uimode -> {
                (if (isDarkTheme()) {
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }).also {
                    with(sharedPref.edit()) {
                        putInt("haha", it)
                        commit()
                    }
                    applyTheme(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    private fun initWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        /*
         * for test change MINUTES
         *
         */
        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            JOB_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }

    companion object {
        const val JOB_TAG = "notificationWorkTag"
        const val KEY_STATE_DETAILS = "KEY_STATE_DETAILS"
    }

}