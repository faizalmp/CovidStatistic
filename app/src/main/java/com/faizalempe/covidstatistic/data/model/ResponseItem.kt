package com.faizalempe.covidstatistic.data.model

import com.squareup.moshi.Json

data class ResponseItem(
        @Json(name = "attributes")
        val attributes: Attributes? = null,
)