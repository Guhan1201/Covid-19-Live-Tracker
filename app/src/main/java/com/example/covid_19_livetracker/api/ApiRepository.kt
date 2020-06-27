package com.example.covid_19_livetracker.api

object ApiRepository  {
    private val apiInterface = ApiClient.buildService(APIInterface::class.java)

    fun getData() = apiInterface.getData()

    fun getStateDistrictData() = apiInterface.getStateDistrictData()

    operator fun invoke(): ApiRepository {
        return this
    }

}