package io.github.masterj3y.sorna.feature.auth

import io.github.masterj3y.sorna.core.utils.AppSession
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val appSession: AppSession) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val isLoggedIn = appSession.isLoggedIn
        val userAccessToken = appSession.accessToken

        var request = chain.request()

        if (isLoggedIn)
            request = request.newBuilder()
                .addHeader(
                    AUTHORIZATION_HEADER_NAME,
                    "$AUTHORIZATION_TOKEN_PREFIX $userAccessToken"
                )
                .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val AUTHORIZATION_TOKEN_PREFIX = "Bearer"
    }

}