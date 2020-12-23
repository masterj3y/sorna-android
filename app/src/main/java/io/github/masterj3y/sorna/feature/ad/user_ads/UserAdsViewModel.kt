package io.github.masterj3y.sorna.feature.ad.user_ads

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class UserAdsViewModel @ViewModelInject constructor(private val repository: AdsRepository) : BaseViewModel() {

    private val getUserAds = MutableLiveData<Boolean>()

    @ExperimentalCoroutinesApi
    val userAds: LiveData<List<Ad>> = getUserAds.switchMap {
        launchOnViewModelScope {
            loading()
            repository.getAllUserAds({ loading(false) }, ::error)
        }
    }

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getUserAds() {
        this.getUserAds.value = true
    }

    private fun loading(loading: Boolean = true) {
        this.loading.postValue(loading)
    }

    private fun error(error: String) {
        loading(false)
        this.error.postValue(error)
    }
}