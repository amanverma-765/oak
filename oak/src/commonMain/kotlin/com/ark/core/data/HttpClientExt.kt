package com.ark.core.data

import com.ark.core.domain.ApiResult
import com.ark.core.domain.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): ApiResult<T, DataError.Remote> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return ApiResult.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        return ApiResult.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return ApiResult.Error(DataError.Remote.UNKNOWN_ERROR)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ApiResult<T, DataError.Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                ApiResult.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                ApiResult.Error(DataError.Remote.SERIALIZATION_ERROR)
            }
        }
        408 -> ApiResult.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> ApiResult.Error(DataError.Remote.TOO_MANY_REQUESTS)
        404 -> ApiResult.Error(DataError.Remote.NOT_FOUND)
        in 500..599 -> ApiResult.Error(DataError.Remote.SERVER_ERROR)
        else -> ApiResult.Error(DataError.Remote.UNKNOWN_ERROR)
    }
}