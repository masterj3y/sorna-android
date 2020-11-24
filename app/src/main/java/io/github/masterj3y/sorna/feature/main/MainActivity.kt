package io.github.masterj3y.sorna.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseActivity
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.databinding.ActivityMainBinding
import io.github.masterj3y.sorna.feature.auth.AuthActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var appSession: AppSession

    override fun layoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (appSession.isLoggedIn.not()) {
            startAuthentication()
            finishAffinity()
        }
    }

    private fun startAuthentication() = AuthActivity.start(this)

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}