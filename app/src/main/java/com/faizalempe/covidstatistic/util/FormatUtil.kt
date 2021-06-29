package com.faizalempe.covidstatistic.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatUtil {

    fun Long?.toFormattedNumber(formatter: NumberFormat): String {
        if (this == null) {
            return "-"
        }
        formatter.maximumFractionDigits = 0
        return formatter.format(this)
    }

    fun Long.toPercentage(): String {
        return "$this%"
    }

    fun Long.toDateFormat(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
    }
}