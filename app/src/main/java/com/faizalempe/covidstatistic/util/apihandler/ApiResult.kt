package com.faizalempe.covidstatistic.util.apihandler

data class ApiResult<out T>(val status: ApiStatus, val data: T?, val error: ApiError?, val message: String?) {

    companion object{
        fun<T> success(data:T): ApiResult<T> =
                ApiResult(
                        status = ApiStatus.SUCCESS,
                        data = data,
                        error = null,
                        message = null
                )
        fun<T> error(error: ApiError?, message: String?): ApiResult<T> =
                ApiResult(
                        status = ApiStatus.ERROR,
                        data = null,
                        error = error,
                        message = message
                )
        fun<T> loading(): ApiResult<T> =
                ApiResult(
                        status = ApiStatus.LOADING,
                        data = null,
                        error = null,
                        message = null
                )
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}