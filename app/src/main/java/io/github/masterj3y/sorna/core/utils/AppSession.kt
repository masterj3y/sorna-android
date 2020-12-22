package io.github.masterj3y.sorna.core.utils

import android.net.Uri
import io.github.masterj3y.sorna.feature.user_profile.UserProfile
import javax.inject.Inject

class AppSession @Inject constructor(private val sharedPref: SharedPref) {

    val isLoggedIn: Boolean
        get() = sharedPref.isLoggedIn

    val accessToken: String
        get() = sharedPref.accessToken

    var userProfile: UserProfile
        get() = UserProfile(
                givenName = userGivenName,
                familyName = userFamilyName,
                displayName = userDisplayName,
                emailAddress = userEmailAddress,
                photoUrl = userPhotoUrl
        )
        set(value) = with(value) {
            userGivenName = givenName
            userFamilyName = familyName
            userDisplayName = displayName
            userEmailAddress = emailAddress
            userPhotoUrl = photoUrl
        }

    var userGivenName: String
        get() = sharedPref.userGivenName
        set(value) {
            sharedPref.userGivenName = value
        }

    var userFamilyName: String
        get() = sharedPref.userFamilyName
        set(value) {
            sharedPref.userFamilyName = value
        }

    var userDisplayName: String
        get() = sharedPref.userDisplayName
        set(value) {
            sharedPref.userDisplayName = value
        }

    var userEmailAddress: String
        get() = sharedPref.userEmailAddress
        set(value) {
            sharedPref.userEmailAddress = value
        }

    var userPhotoUrl: Uri
        get() = Uri.parse(sharedPref.userPhotoUrl)
        set(value) {
            sharedPref.userPhotoUrl = value.toString()
        }

    fun login(accessToken: String) {
        sharedPref.accessToken = accessToken
    }

    fun logout() {
        sharedPref.accessToken = ""
    }
}