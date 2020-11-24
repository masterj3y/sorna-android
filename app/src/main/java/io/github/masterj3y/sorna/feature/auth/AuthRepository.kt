package io.github.masterj3y.sorna.feature.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AuthRepository(private val service: AuthService) {

    @ExperimentalCoroutinesApi
    fun signIn(googleIdToken: String,
               onSuccess: (accessToken: String) -> Unit,
               onError: (error: String) -> Unit) = flow {

        val apiResponse = service.loginWithGoogle(googleIdToken)
        val headers = apiResponse.headers()
        val accessToken = headers["Authorization"]
        if (apiResponse.isSuccessful && accessToken != null) {
            emit(true)
            onSuccess(accessToken)
        } else {
            onError("Sign in failed")
        }
    }.catch { e ->
        e.message?.let {
            onError("Sign in failed")
        }
    }
}