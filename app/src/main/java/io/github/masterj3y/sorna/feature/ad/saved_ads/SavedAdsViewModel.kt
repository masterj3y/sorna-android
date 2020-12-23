package io.github.masterj3y.sorna.feature.ad.saved_ads

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SavedAdsViewModel @ViewModelInject constructor(private val repository: AdsRepository) :
    BaseViewModel() {

    private val getSavedAds = MutableLiveData<Boolean>()

    @ExperimentalCoroutinesApi
    val savedAds: LiveData<List<Ad>> = getSavedAds.switchMap {
        loading()
        launchOnViewModelScope {
            repository.getAllSavedAds({ loading(false) }, ::error)
        }
    }

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getSavedAds() {
        this.getSavedAds.value = true
    }

    private fun loading(loading: Boolean = true) {
        this.loading.value = loading
    }

    private fun error(error: String) {
        loading(false)
        this.error.value = error
    }
}