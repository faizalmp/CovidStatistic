package com.faizalempe.covidstatistic.data.model

import com.squareup.moshi.Json

data class Attributes(
        @Json(name = "OBJECTID")
        val id: Long? = null,
        @Json(name = "Country_Region")
        val country: String? = null,
        @Json(name = "Last_Update")
        val lastUpdate: Long? = null,
        @Json(name = "Lat")
        val lat: Double? = null,
        @Json(name = "Long_")
        val lng: Double? = null,
        @Json(name = "Confirmed")
        val confirmed: Long? = null,
        @Json(name = "Deaths")
        val deaths: Long? = null,
        @Json(name = "Recovered")
        val recovered: Long? = null,
        @Json(name = "Active")
        val active: Long? = null
)