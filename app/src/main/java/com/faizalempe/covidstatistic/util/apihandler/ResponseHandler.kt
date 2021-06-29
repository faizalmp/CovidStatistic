package com.faizalempe.covidstatistic.util.apihandler

import com.faizalempe.covidstatistic.util.*
import com.faizalempe.covidstatistic.util.helper.RetrofitHelper.retrofitInstance
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

object ResponseHandler {

    suspend fun <T> getResponse(request: suspend () -> Response<T>): ApiResult<T?> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return ApiResult.success(result.body())
            } else {
                val error = parseError(result)
                ApiResult.error(error, error?.message ?: "Error get remote result")
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            val message = "Can't reach server\nPlease check your internet connection"
            ApiResult.error(ApiError(code = UNKNOWN_HOST_ERROR, message = message), message)
        } catch (e: HttpException) {
            e.printStackTrace()
            ApiResult.error(ApiError(code = e.code(), message = httpErrorMessage(e.code())), httpErrorMessage(e.code()))
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.error(ApiError(code = UNKNOWN_ERROR, message = e.localizedMessage), e.localizedMessage)
        }
    }

    private fun parseError(response: Response<*>): ApiError? {
        val converter = retrofitInstance.responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiError()
        }
    }

    private fun httpErrorMessage(code: Int): String {
        return when (code) {
            HTTP_ERROR_BAD_REQUEST -> "Check your data you send"
            HTTP_ERROR_UNAUTHORIZED -> "Your session has ended. Please login again"
            HTTP_ERROR_FORBIDDEN -> "Forbidden"
            HTTP_ERROR_NOT_FOUND -> "Server can't find what you want"
            HTTP_ERROR_METHOD_NOT_ALLOWED -> "Method not allowed"
            HTTP_ERROR_NOT_ACCEPTABLE -> "Not acceptable"
            HTTP_ERROR_PROXY_AUTHENTICATION_REQUIRED -> "Need Proxy"
            HTTP_ERROR_REQUEST_TIMEOUT -> "Timeout"
            HTTP_ERROR_CONFLICT -> "Conflict"
            HTTP_ERROR_GONE -> "The resources you wanted has gone"
            HTTP_ERROR_LENGTH_REQUIRED -> "Length required"
            HTTP_ERROR_PRECONDITION_FAILED -> "Precondition Failed"
            HTTP_ERROR_REQUEST_ENTITY_TOO_LARGE -> "Payload too large"
            HTTP_ERROR_REQUEST_URL_TOO_LONG -> "URI too long"
            HTTP_ERROR_UNSUPPORTED_MEDIA_TYPE -> "Unsupported media type"
            HTTP_ERROR_REQUEST_RANGE_NOT_SATISFIABLE -> "Range not satisfiable"
            HTTP_ERROR_EXPECTATION_FAILED -> "Expectation Failed"
            HTTP_ERROR_MISDIRECTED_REQUEST -> "Misdirected Request"
            HTTP_ERROR_INTERNAL_SERVER -> "Something wrong in server"
            HTTP_ERROR_NOT_IMPEMENTED -> "Something wrong in server"
            HTTP_ERROR_BAD_GATEWAY -> "Something wrong in server"
            HTTP_ERROR_SERVICE_UNAVAILABLE -> "Something wrong in server"
            HTTP_ERROR_GATEWAY_TIMEOUT -> "Something wrong in server"
            HTTP_ERROR_HTTP_VERSION_NOT_SUPPORTED -> "Something wrong in server"
            HTTP_ERROR_VARIANT_ALSO_NEGOTIATES -> "Something wrong in server"
            HTTP_ERROR_INSUFFICIENT_STORAGE -> "Something wrong in server"
            HTTP_ERROR_LOOP_DETECTED -> "Something wrong in server"
            HTTP_ERROR_NOT_EXTENDED -> "Something wrong in server"
            HTTP_ERROR_NETWORK_AUTH_REQUIRED -> "Something wrong in server"
            else -> "Unknown error"
        }
    }
}