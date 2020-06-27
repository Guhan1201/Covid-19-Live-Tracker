package com.example.covid_19_livetracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_19_livetracker.api.ApiRepository
import com.example.covid_19_livetracker.model.StateResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {

    private val apiRepository =
        ApiRepository()
    private var _stateResponse = MutableLiveData<StateResponse>()
    val stateResponse : LiveData<StateResponse> = _stateResponse
    private var _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading
    private val disposable : CompositeDisposable = CompositeDisposable()

    init {
        getData()
    }

    fun getData() {
        disposable.add(apiRepository.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _loading.value = true
            }
            .doFinally {
                _loading.value = false
            }
            .subscribe({
             _stateResponse.value = it
                Log.e("TESt Success",it.stateWiseDetails.toString())
                Log.e("TESt Success sedd",it.toString())
            },{
                Log.e("TESt Error",it.toString())
            }))
    }
}
