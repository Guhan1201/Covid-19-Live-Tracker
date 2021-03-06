package com.example.covid_19_livetracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_19_livetracker.api.ApiRepository
import com.example.covid_19_livetracker.model.StateDetailsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StateDetailPageViewModel : ViewModel() {

    val apiRepository = ApiRepository()

    private var _stateResponse = MutableLiveData<StateDetailsResponse>()
    val stateResponse : LiveData<StateDetailsResponse> = _stateResponse
    private var _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading
    private val disposable : CompositeDisposable = CompositeDisposable()


    fun getStateDetail(state: String?) {
     disposable.add(apiRepository.getStateDistrictData()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe {
              _loading.value = true
          }
          .doFinally {
              _loading.value = false
          }
          .subscribe({
              val data = it.find { it.state == state }
              _stateResponse.value = data

          },{
              Log.e("HAAHAHAHAHHA",it.toString())
          }))
    }
}