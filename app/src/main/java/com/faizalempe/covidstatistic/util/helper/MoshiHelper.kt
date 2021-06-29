package com.faizalempe.covidstatistic.util.helper

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object MoshiHelper {
    private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    fun getMoshiConverterFactory() : MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }
}