package com.faizalempe.covidstatistic.util.helper

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkhttpHelper {
    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()

    fun getHttpClient() : OkHttpClient.Builder {
        return httpClient
    }

    fun getHttpLogging() : HttpLoggingInterceptor {
        return logging
    }
}