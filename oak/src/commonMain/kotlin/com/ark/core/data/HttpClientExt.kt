package com.ark.core.data

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

internal suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): ApiResponse<T, DataError.Remote> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return ApiResponse.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        return ApiResponse.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return ApiResponse.Error(DataError.Remote.UNKNOWN_ERROR)
    }

    return responseToResult(response)
}

internal suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ApiResponse<T, DataError.Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                ApiResponse.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                ApiResponse.Error(DataError.Remote.SERIALIZATION_ERROR)
            }
        }
        408 -> ApiResponse.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> ApiResponse.Error(DataError.Remote.TOO_MANY_REQUESTS)
        404 -> ApiResponse.Error(DataError.Remote.NOT_FOUND)
        in 500..599 -> ApiResponse.Error(DataError.Remote.SERVER_ERROR)
        else -> ApiResponse.Error(DataError.Remote.UNKNOWN_ERROR)
    }
}