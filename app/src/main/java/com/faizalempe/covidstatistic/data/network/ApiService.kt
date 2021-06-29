package com.faizalempe.covidstatistic.data.network

import com.faizalempe.covidstatistic.data.model.ResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getData(): Response<List<ResponseItem>>
}