package io.github.masterj3y.sorna.core.utils

import javax.inject.Inject

class AppSession @Inject constructor(private val sharedPref: SharedPref) {

    var nightModeEnabled: Boolean
        get() = sharedPref.nightModeEnabled
        set(value) {
            sharedPref.nightModeEnabled = value
        }

    var downloadFabPositionX: Int
        get() = sharedPref.downloadFabPositionX
        set(value) {
            sharedPref.downloadFabPositionX = value
        }

    var downloadFabPositionY: Int
        get() = sharedPref.downloadFabPositionY
        set(value) {
            sharedPref.downloadFabPositionY = value
        }

    companion object {
        const val GUIDE_ENABLE_TAP_TO_DOWNLOAD = "GUIDE_ENABLE_TAP_TO_DOWNLOAD"
        const val GUIDE_SETTINGS = "GUIDE_SETTINGS"
    }
}