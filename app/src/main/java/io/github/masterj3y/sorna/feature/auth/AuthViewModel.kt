package io.github.masterj3y.sorna.feature.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.core.utils.AppSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
        private val repository: AuthRepository,
        private val appSession: AppSession) : BaseViewModel() {

    private var loginJob: Job? = null

    val loading = MutableLiveData(false)
    val error = MutableLiveData("")

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean> get() = _isSignedIn

    @ExperimentalCoroutinesApi
    fun signIn(account: GoogleSignInAccount) {
        account.idToken?.let { idToken ->
            loading()
            loginJob = viewModelScope.launch {
                repository.signIn(googleIdToken = idToken, onSuccess = ::onSuccess, onError = ::onError)
            }
        }
        saveUserProfile(account)
    }

    fun cancelSignIn() {
        loginJob?.cancel()
        loading(false)
    }

    private fun loading(visible: Boolean = true) {
        this.loading.value = visible
    }

    private fun onSuccess(accessToken: String) {
        loading(false)
        _isSignedIn.value =true
        appSession.login(accessToken)
    }

    private fun onError(message: String) {
        loading(false)
        this.error.value = message
    }

    private fun saveUserProfile(account: GoogleSignInAccount) = with(appSession) {
        account.givenName?.let { userGivenName = it }
        account.familyName?.let { userFamilyName = it }
        account.displayName?.let { userDisplayName = it }
        account.email?.let { userEmailAddress = it }
        account.photoUrl?.let { userPhotoUrl = it }
    }
}