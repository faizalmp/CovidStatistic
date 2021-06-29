package com.faizalempe.covidstatistic.data.repository

import com.faizalempe.covidstatistic.data.model.ResponseItem
import com.faizalempe.covidstatistic.data.remote.RemoteDataSource
import com.faizalempe.covidstatistic.util.apihandler.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DataRepository {

    private val dataSource = RemoteDataSource()

    fun getData(): Flow<ApiResult<List<ResponseItem>?>> {
        return flow {
            emit(ApiResult.loading())
            emit(dataSource.getData())
        }.flowOn(Dispatchers.IO)
    }
}