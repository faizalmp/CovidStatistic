package com.faizalempe.covidstatistic.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.faizalempe.covidstatistic.R
import com.faizalempe.covidstatistic.data.model.Attributes
import com.faizalempe.covidstatistic.databinding.FragmentHomeBinding
import com.faizalempe.covidstatistic.ui.MainActivity
import com.faizalempe.covidstatistic.util.FormatUtil.toDateFormat
import com.faizalempe.covidstatistic.util.FormatUtil.toFormattedNumber
import com.faizalempe.covidstatistic.util.FormatUtil.toPercentage
import com.faizalempe.covidstatistic.util.PrefManager
import com.faizalempe.covidstatistic.util.apihandler.ApiStatus
import com.faizalempe.covidstatistic.viewmodel.HomeViewModel
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var countryList: ArrayList<String?>
    private lateinit var langOption: Array<String>
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        prefManager = PrefManager(requireContext())

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)

        viewModel.getStatisticData()
        initView()
        observeValue()
    }

    private fun initView() {
        binding.apply {
            homeSelectCountry.setOnClickListener {
                homeSelectCountry.showDropDown()
            }

            if (prefManager.lang == "in") {
                homeSelectLang.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_id), null, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_filled), null)
                homeSelectLang.text = SpannableStringBuilder(getString(R.string.default_lang))
            } else {
                homeSelectLang.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_us), null, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_filled), null)
                homeSelectLang.text = SpannableStringBuilder(getString(R.string.default_lang))
            }

            langOption = resources.getStringArray(R.array.lang_list)
            val adapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_dropdown_item_1line, langOption)
            homeSelectLang.setAdapter(adapter)

            homeSelectLang.setOnClickListener {
                homeSelectLang.showDropDown()
            }

            homeSelectLang.addTextChangedListener {
                var lang = it.toString().toLowerCase(Locale.ROOT)
                if (lang == "id") {
                    lang = "in"
                    homeSelectLang.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_id), null, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_filled), null)
                } else {
                    homeSelectLang.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.ic_us), null, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down_filled), null)
                }
                prefManager.lang = lang
                requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }

            homeSelectCountry.addTextChangedListener {
                val data = viewModel.getByCountry(it.toString())
                assignData(data)
            }
        }
    }

    private fun observeValue() {
        viewModel.response.observe(viewLifecycleOwner, Observer {res ->
            try {
                when (res.status) {
                    ApiStatus.SUCCESS -> {
                        val data = viewModel.getByCountry(binding.homeSelectCountry.text.toString())
                        assignData(data)
                        countryList = ArrayList(res.data?.map { it.attributes?.country }?.sortedBy { it })
                        val adapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_dropdown_item_1line, countryList)
                        binding.homeSelectCountry.setAdapter(adapter)
                        viewModel.getByCountry(binding.homeSelectCountry.text.toString())
                    }
                    ApiStatus.LOADING -> {
                        // wait for response
                    }
                    ApiStatus.ERROR -> {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun assignData(data: Attributes?) {
        binding.apply {
            val formatter = NumberFormat.getNumberInstance()
            homeTxtNumberRecovered.text = data?.recovered?.toFormattedNumber(formatter)
            homeTxtNumberAffected.text = data?.active?.toFormattedNumber(formatter)
            homeActiveCaseTxtNumber.text = data?.active?.toFormattedNumber(formatter)
            homeRecoveredTxtNumber.text = data?.recovered?.toFormattedNumber(formatter)
            homeTotalCaseTxtNumber.text = data?.confirmed?.toFormattedNumber(formatter)
            homeDeathTxtNumber.text = data?.deaths?.toFormattedNumber(formatter)
            if (data?.recovered != null && data.active != null && data.confirmed != null) {
                val recoveryRatio = data.recovered.times(100).div(data.confirmed)
                val affectedRatio = data.active.times(100).div(data.confirmed)
                homeTxtProgress.text = recoveryRatio.toPercentage()
                homeProgressRecovered.progress = recoveryRatio.toInt()
                homeProgressAffected.progress = affectedRatio.toInt()
            } else {
                homeTxtProgress.text = getString(R.string.default_number)
                homeProgressRecovered.progress = 0
                homeProgressAffected.progress = 0
            }
            homeTxtLabelLastUpdate.text = String.format(getString(R.string.label_last_update, data?.lastUpdate?.toDateFormat()))
        }
    }
}