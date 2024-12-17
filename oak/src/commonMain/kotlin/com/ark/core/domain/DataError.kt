package com.ark.core.domain

sealed interface DataError: Error {
    sealed interface Remote : DataError {
        data object RequestTimeout : Remote
        data object TooManyRequests : Remote
        data object NoInternet : Remote
        data object ServerError : Remote
        data object SerializationError : Remote
        data object NotFound : Remote
        data class UnknownError(val message: String? = null) : Remote
    }
}