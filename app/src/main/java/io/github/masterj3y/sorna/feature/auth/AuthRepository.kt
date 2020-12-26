package io.github.masterj3y.sorna.feature.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val service: AuthService) {

    @ExperimentalCoroutinesApi
    suspend fun signIn(googleIdToken: String,
               onSuccess: (accessToken: String) -> Unit,
               onError: (error: String) -> Unit)  {

        try {
            val apiResponse = service.loginWithGoogle(googleIdToken)
            val headers = apiResponse.headers()
            val accessToken = headers["Authorization"]
            if (apiResponse.isSuccessful && accessToken != null) {
                onSuccess(accessToken)
            } else {
                onError("Sign in failed")
            }
        } catch (e: Exception) {
            e.message?.let {
                onError("Sign in failed")
            }
        }
    }
}