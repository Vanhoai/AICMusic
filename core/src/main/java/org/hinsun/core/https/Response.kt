package org.hinsun.core.https

sealed class BaseResponse {
    abstract val statusCode: Int
    abstract val message: String
}

data class Response<T>(
    override val statusCode: Int,
    override val message: String,
    val payload: T
) : BaseResponse()


data class Failure(
    override val statusCode: Int,
    override val message: String,
) : BaseResponse()