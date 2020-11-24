package io.github.masterj3y.sorna.feature.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.core.utils.AppSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

class AuthViewModel @ViewModelInject constructor(
        private val repository: AuthRepository,
        private val appSession: AppSession) : BaseViewModel() {

    private val googleIdToken = MutableLiveData<String>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData("")

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    val isSignedIn: LiveData<Boolean> = googleIdToken.switchMap {
        loading()
        launchOnViewModelScope {
            repository.signIn(
                    googleIdToken = it,
                    onSuccess = { accessToken -> onSuccess(accessToken) },
                    onError = { error -> onError(error) }
            )
        }
    }

    fun signIn(googleIdToken: String) {
        this.googleIdToken.value = googleIdToken
    }

    private fun loading(visible: Boolean = true) {
        this.loading.value = visible
    }

    private fun onSuccess(accessToken: String) {
        loading(false)
        appSession.login(accessToken)
    }

    private fun onError(message: String) {
        loading(false)
        this.error.value = message
    }
}