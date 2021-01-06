package io.github.masterj3y.sorna.core.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.github.masterj3y.sorna.feature.auth.AuthActivity
import io.github.masterj3y.sorna.feature.user_profile.UserProfile
import javax.inject.Inject

class AppSession @Inject constructor(
    private val sharedPref: SharedPref,
    private val googleSignInClient: GoogleSignInClient
) {

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

    var nightModeEnabled: Boolean
        get() = sharedPref.nightModeEnabled
        set(value) {
            sharedPref.nightModeEnabled = value
        }

    var appLanguage: String
        get() = sharedPref.appLanguage
        set(value) {
            sharedPref.appLanguage = value
        }

    fun login(accessToken: String) {
        sharedPref.accessToken = accessToken
    }

    fun logout(activity: Activity) {
        activity.run {
            googleSignInClient.signOut()
                .addOnCompleteListener {
                    sharedPref.accessToken = ""
                    AuthActivity.start(this)
                    finishAffinity()
                }
        }
    }
}