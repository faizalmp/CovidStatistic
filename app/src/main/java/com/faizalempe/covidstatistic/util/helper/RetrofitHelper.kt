package com.faizalempe.covidstatistic.util.helper

import com.faizalempe.covidstatistic.BuildConfig
import com.faizalempe.covidstatistic.data.network.ApiService
import com.faizalempe.covidstatistic.util.helper.MoshiHelper.getMoshiConverterFactory
import com.faizalempe.covidstatistic.util.helper.OkhttpHelper.getHttpClient
import com.faizalempe.covidstatistic.util.helper.OkhttpHelper.getHttpLogging
import retrofit2.Retrofit

object RetrofitHelper {

    private fun retrofitService(baseUrl: String) : Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(getMoshiConverterFactory())
                .client(getHttpClient().addInterceptor(getHttpLogging()).build())
                .baseUrl(baseUrl)
                .build()
    }
    val retrofitInstance: Retrofit =
            retrofitService(
                    BuildConfig.BASE_URL
            )

    val retrofitApiService: ApiService = retrofitService(
            BuildConfig.BASE_URL
    ).create(ApiService::class.java)
}