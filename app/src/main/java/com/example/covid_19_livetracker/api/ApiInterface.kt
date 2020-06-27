package com.example.covid_19_livetracker.api

import com.example.covid_19_livetracker.model.StateDetailsResponse
import com.example.covid_19_livetracker.model.StateResponse
import io.reactivex.Single
import retrofit2.http.GET

interface APIInterface {

    @GET("data.json")
    fun getData(): Single<StateResponse>

    @GET("v2/state_district_wise.json")
    fun getStateDistrictData(): Single<List<StateDetailsResponse>>

}