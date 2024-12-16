package com.ark.core.domain

sealed interface ApiResponse<out D, out E: Error> {
    data class Success<out D>(val data: D): ApiResponse<D, Nothing>
    data class Error<out E: com.ark.core.domain.Error>(val error: E):
        ApiResponse<Nothing, E>
}

inline fun <T, E: Error, R> ApiResponse<T, E>.map(map: (T) -> R): ApiResponse<R, E> {
    return when(this) {
        is ApiResponse.Error -> ApiResponse.Error(error)
        is ApiResponse.Success -> ApiResponse.Success(map(data))
    }
}

fun <T, E: Error> ApiResponse<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: Error> ApiResponse<T, E>.onSuccess(action: (T) -> Unit): ApiResponse<T, E> {
    return when(this) {
        is ApiResponse.Error -> this
        is ApiResponse.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T, E: Error> ApiResponse<T, E>.onError(action: (E) -> Unit): ApiResponse<T, E> {
    return when(this) {
        is ApiResponse.Error -> {
            action(error)
            this
        }
        is ApiResponse.Success -> this
    }
}

typealias EmptyResult<E> = ApiResponse<Unit, E>