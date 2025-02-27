package org.hinsun.core.https

sealed class HttpResponse<T> {
    data class HttpProcess<T>(val data: T? = null) : HttpResponse<T>()
    data class HttpSuccess<T>(val data: T) : HttpResponse<T>()
    data class HttpFailure<T>(val failure: Failure) : HttpResponse<T>()
}