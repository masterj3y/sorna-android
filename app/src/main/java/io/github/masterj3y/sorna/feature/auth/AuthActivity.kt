package io.github.masterj3y.sorna.feature.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseActivity
import io.github.masterj3y.sorna.databinding.ActivityAuthBinding
import io.github.masterj3y.sorna.feature.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int = R.layout.activity_auth

    private val googleSignInOption by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("809003685834-bqkuodq77dkn38iur2u4ii1ocggrbf8o.apps.googleusercontent.com")
                .requestEmail()
                .build()
    }

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(this, googleSignInOption)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        completedTask.getResult(ApiException::class.java)?.let { onAccountLoaded(it) }
    } catch (e: ApiException) {
    }

    private fun onAccountLoaded(account: GoogleSignInAccount) =
            account.idToken?.let { viewModel.signIn(it) }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            signIn.setOnClickListener {
                signIn()
            }
        }
        subscribeObservers()
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    private fun subscribeObservers() {
        viewModel.isSignedIn.observe(this, ::onSignedIn)
        viewModel.error.observe(this, ::onError)
    }

    private fun onSignedIn(isSignedIn: Boolean) {
        if (isSignedIn) {
            MainActivity.start(this)
            finishAffinity()
        }
    }

    private fun onError(message: String) {
        if (message.isNotBlank())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun signIn() = startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)

    companion object {

        private const val RC_SIGN_IN = 256

        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }
}