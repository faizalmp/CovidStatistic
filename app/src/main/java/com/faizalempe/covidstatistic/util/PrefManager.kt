package com.faizalempe.covidstatistic.util

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {

    companion object {
        const val PREF_NAME = "COVID_STATISTIC"
        const val LANG = "LANG"
    }

    private val sp: SharedPreferences by lazy {
        context.applicationContext!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val spe: SharedPreferences.Editor by lazy {
        sp.edit()
    }

    fun clear() {
        sp.edit().clear().apply()
    }

    var lang: String?
        get() = sp.getString(LANG, "en")
        set(value) {
            spe.putString(LANG, value)
            spe.commit()
        }
}