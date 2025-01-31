package org.hinsun.core.https


data class Response<T>(
    val status: Int,
    val message: String,
    val payload: T? = null
) {
    fun isSuccess(): Boolean {
        return HttpStatusCode.isSuccess(status)
    }

    fun isFailure(): Boolean {
        return !HttpStatusCode.isSuccess(status)
    }
}

interface Failure {
    val cause: Throwable?

    data class RequestFailure(
        val status: Int,
        val message: String? = "Request failed",
        override val cause: Throwable? = null
    ) : Failure

    data class IOFailure(
        val status: Int,
        val message: String? = "IO failed",
        override val cause: Throwable? = null
    ) : Failure

    data class ExceptionFailure(
        val status: Int,
        val message: String? = "Exception failed",
        override val cause: Throwable? = null
    ) : Failure
}