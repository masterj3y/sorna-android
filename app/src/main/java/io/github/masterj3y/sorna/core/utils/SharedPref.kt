package io.github.masterj3y.sorna.core.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SharedPref @Inject constructor(application: Application) {

    private val preferences: SharedPreferences =
            application.getSharedPreferences("pref", Context.MODE_PRIVATE)

    val isLoggedIn: Boolean
        get() = accessToken.isNotEmpty()

    var accessToken: String
        get() = getString(ACCESS_TOKEN to "")
        set(value) = put(ACCESS_TOKEN to value)

    var userGivenName: String
        get() = getString(USER_GIVEN_NAME to "Unknown")
        set(value) = put(USER_GIVEN_NAME to value)

    var userFamilyName: String
        get() = getString(USER_FAMILY_NAME to "Unknown")
        set(value) = put(USER_FAMILY_NAME to value)

    var userDisplayName: String
        get() = getString(USER_DISPLAY_NAME to "Unknown")
        set(value) = put(USER_DISPLAY_NAME to value)

    var userEmailAddress: String
        get() = getString(USER_EMAIL_ADDRESS to "Unknown")
        set(value) = put(USER_EMAIL_ADDRESS to value)

    var userPhotoUrl: String
        get() = getString(USER_PHOTO_URL to "Null")
        set(value) = put(USER_PHOTO_URL to value)

    var nightModeEnabled: Boolean
        get() = getBoolean(NIGHT_MODE_ENABLED to false)
        set(value) = put(NIGHT_MODE_ENABLED to value)

    private fun getString(pair: Pair<String, String>): String =
            preferences.getString(pair.first, pair.second) ?: pair.second

    private fun getBoolean(pair: Pair<String, Boolean>): Boolean =
            preferences.getBoolean(pair.first, pair.second)

    private fun <T> put(data: Pair<String, T>) {
        when (data.second) {
            is String -> edit { putString(data.first, data.second as String) }
            is Int -> edit { putInt(data.first, data.second as Int) }
            is Long -> edit { putLong(data.first, data.second as Long) }
            is Float -> edit { putFloat(data.first, data.second as Float) }
            is Boolean -> edit { putBoolean(data.first, data.second as Boolean) }
        }
    }

    private fun edit(action: SharedPreferences.Editor.() -> Unit) =
            preferences.edit { action() }

    companion object {
        private const val ACCESS_TOKEN = "at"
        private const val USER_GIVEN_NAME = "user_given_name"
        private const val USER_FAMILY_NAME = "user_family_name"
        private const val USER_DISPLAY_NAME = "user_display_name"
        private const val USER_EMAIL_ADDRESS = "user_email_address"
        private const val USER_PHOTO_URL = "user_photo_url"
        private const val NIGHT_MODE_ENABLED = "night_mode_enabled"
    }
}