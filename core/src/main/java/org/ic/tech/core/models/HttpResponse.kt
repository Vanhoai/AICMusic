package org.ic.tech.core.models

sealed class HttpResponse<T> {
    data class HttpProcess<T>(val data: T? = null) : HttpResponse<T>()
    data class HttpSuccess<T>(val data: T) : HttpResponse<T>()
    data class HttpFailure<T>(val e: Throwable) : HttpResponse<T>()
}