package com.faizalempe.covidstatistic.data.remote

import com.faizalempe.covidstatistic.data.model.ResponseItem
import com.faizalempe.covidstatistic.util.apihandler.ApiResult
import com.faizalempe.covidstatistic.util.apihandler.ResponseHandler.getResponse
import com.faizalempe.covidstatistic.util.helper.RetrofitHelper.retrofitApiService

class RemoteDataSource {

    suspend fun getData(): ApiResult<List<ResponseItem>?> {
        return getResponse { retrofitApiService.getData() }
    }

}