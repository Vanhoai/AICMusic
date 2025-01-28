package org.hinsun.infrastructure.datasources.remote

import org.hinsun.core.https.Response
import org.hinsun.domain.models.OAuthResponse
import retrofit2.http.GET
import retrofit2.http.POST

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface AuthRemoteDatasource {
    @POST("/oauth")
    suspend fun oauth(): Response<OAuthResponse>

    @GET("https://jsonplaceholder.typicode.com/posts")
    suspend fun getPosts(): List<Post>
}