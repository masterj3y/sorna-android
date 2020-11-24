package io.github.masterj3y.sorna.core.utils

import javax.inject.Inject

class AppSession @Inject constructor(private val sharedPref: SharedPref) {

    val isLoggedIn: Boolean
        get() = sharedPref.isLoggedIn

    val accessToken: String
        get() = sharedPref.accessToken

    fun login(accessToken: String) {
        sharedPref.accessToken = accessToken
    }

    fun logout() {
        sharedPref.accessToken = ""
    }
}