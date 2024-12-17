package com.ark.core.data

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.core.domain.Error
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
    } catch (ex: SocketTimeoutException) {
        return ApiResponse.Error(DataError.Remote.RequestTimeout)
    } catch (ex: UnresolvedAddressException) {
        return ApiResponse.Error(DataError.Remote.NoInternet)
    } catch (ex: Exception) {
        coroutineContext.ensureActive()
        return ApiResponse.Error(DataError.Remote.UnknownError(ex.message ?: "Unknown Error"))
    }

    return responseToResult(response)
}

private suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ApiResponse<T, DataError.Remote> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                ApiResponse.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                ApiResponse.Error(DataError.Remote.SerializationError)
            }
        }

        408 -> ApiResponse.Error(DataError.Remote.RequestTimeout)
        429 -> ApiResponse.Error(DataError.Remote.TooManyRequests)
        404 -> ApiResponse.Error(DataError.Remote.NotFound)
        in 500..599 -> ApiResponse.Error(DataError.Remote.ServerError)
        else -> ApiResponse.Error(DataError.Remote.UnknownError())
    }
}

internal inline fun <T, R, E : Error> ApiResponse<T, E>.mapSuccess(transform: (T) -> R): ApiResponse<R, E> {
    return when (this) {
        is ApiResponse.Success -> ApiResponse.Success(transform(this.data))
        is ApiResponse.Error -> ApiResponse.Error(this.error)
    }
}