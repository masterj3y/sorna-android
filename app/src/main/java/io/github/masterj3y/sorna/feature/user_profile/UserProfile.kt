package io.github.masterj3y.sorna.feature.user_profile

import android.net.Uri

data class UserProfile(
        val givenName: String,
        val familyName: String,
        val displayName: String,
        val emailAddress: String,
        val photoUrl: Uri
)
