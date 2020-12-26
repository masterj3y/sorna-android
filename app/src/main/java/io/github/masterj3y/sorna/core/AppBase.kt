package io.github.masterj3y.sorna.core

import android.app.Activity
import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppBase : Application() {

    companion object {

        private const val REQUEST_ID_TOKEN = "809003685834-bqkuodq77dkn38iur2u4ii1ocggrbf8o.apps.googleusercontent.com"
        private val googleSignInOption by lazy {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(REQUEST_ID_TOKEN)
                    .requestEmail()
                    .build()
        }

        fun getGoogleSignInClient(activity: Activity) = GoogleSignIn.getClient(activity, googleSignInOption)
    }
}