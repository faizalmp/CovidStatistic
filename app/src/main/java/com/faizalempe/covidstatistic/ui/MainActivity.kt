package com.faizalempe.covidstatistic.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.faizalempe.covidstatistic.R
import com.faizalempe.covidstatistic.databinding.ActivityMainBinding
import com.faizalempe.covidstatistic.util.ContextUtil
import com.faizalempe.covidstatistic.util.PrefManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        navController = findNavController(R.id.nav_host)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navController.navigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    override fun attachBaseContext(newBase: Context?) {
        prefManager = PrefManager(newBase!!)
        val localeToSwitchTo = Locale(prefManager?.lang)
        val localeUpdatedContext: ContextWrapper = ContextUtil.updateLocale(newBase!!, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}