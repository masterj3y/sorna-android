package io.github.masterj3y.sorna.feature.auth

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("auth/google")
    suspend fun loginWithGoogle(@Header("googleIdToken") googleIdToken: String): Response<String>
}