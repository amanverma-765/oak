package com.ark.core.domain

sealed interface DataError: Error {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION_ERROR,
        NOT_FOUND,
        UNKNOWN_ERROR
    }
}