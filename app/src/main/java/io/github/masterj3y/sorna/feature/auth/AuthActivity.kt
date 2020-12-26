package io.github.masterj3y.sorna.feature.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.toast
import io.github.masterj3y.sorna.core.platform.BaseActivity
import io.github.masterj3y.sorna.databinding.ActivityAuthBinding
import io.github.masterj3y.sorna.feature.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val model: AuthViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun layoutRes(): Int = R.layout.activity_auth

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    @ExperimentalCoroutinesApi
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        completedTask.getResult(ApiException::class.java)?.let { onAccountLoaded(it) }
    } catch (e: ApiException) {
    }

    @ExperimentalCoroutinesApi
    private fun onAccountLoaded(account: GoogleSignInAccount) = model.signIn(account)

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@AuthActivity
            viewModel = model
            signIn.setOnClickListener { signIn() }
            cancel.setOnClickListener { cancelSignIn() }
            exit.setOnClickListener { finishAffinity() }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        model.isSignedIn.observe(this, ::onSignedIn)
        model.error.observe(this, ::onError)
    }

    private fun onSignedIn(isSignedIn: Boolean) {
        if (isSignedIn) {
            MainActivity.start(this)
            finishAffinity()
        }
    }

    private fun onError(message: String) {
        if (message.isNotBlank()) {
            cancelSignIn()
            toast(R.string.network_error)
        }
    }

    private fun signIn() = startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)

    private fun cancelSignIn() {
        model.cancelSignIn()
        googleSignInClient.signOut()
    }

    companion object {

        private const val RC_SIGN_IN = 256

        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }
}