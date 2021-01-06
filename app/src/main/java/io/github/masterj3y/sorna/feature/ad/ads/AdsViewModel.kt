package io.github.masterj3y.sorna.feature.ad.ads

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AdsViewModel @ViewModelInject constructor(
        private val repository: AdsRepository
) : BaseViewModel() {

    private val getAds = MutableLiveData(false)

    @ExperimentalCoroutinesApi
    private val _ads: MutableLiveData<List<Ad>> = getAds.switchMap {
        loading()
        launchOnViewModelScope {
            repository.getAllAds(onSuccess = ::success, onError = ::error)
        }
    } as MutableLiveData<List<Ad>>

    @ExperimentalCoroutinesApi
    val ads: LiveData<List<Ad>> get() = _ads

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getAds() {
        this.getAds.value = true
    }

    private fun loading(isLoading: Boolean = true) {
        this.loading.postValue(isLoading)
    }

    private fun success() {
        loading(false)
        error.value = null
    }

    @ExperimentalCoroutinesApi
    private fun error(message: String) {
        loading(false)
        this._ads.value = listOf()
        this.error.value = message
    }
}