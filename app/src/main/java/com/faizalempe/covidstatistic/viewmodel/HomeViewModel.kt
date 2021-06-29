package com.faizalempe.covidstatistic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faizalempe.covidstatistic.data.model.Attributes
import com.faizalempe.covidstatistic.data.model.ResponseItem
import com.faizalempe.covidstatistic.data.repository.DataRepository
import com.faizalempe.covidstatistic.util.apihandler.ApiResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val repository = DataRepository()

    private val _response = MutableLiveData<ApiResult<List<ResponseItem>?>>()
    val response: LiveData<ApiResult<List<ResponseItem>?>>
        get() = _response

    fun getStatisticData() {
        viewModelScope.launch {
            repository.getData().collect { result ->
                _response.value = result
            }
        }
    }

    fun getByCountry(country: String): Attributes? {
        return _response.value?.data?.find { data -> data.attributes?.country == country }?.attributes
    }
}