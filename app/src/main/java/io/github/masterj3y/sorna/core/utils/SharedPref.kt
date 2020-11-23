package io.github.masterj3y.sorna.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPref(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("pref", Context.MODE_PRIVATE)

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

    }
}