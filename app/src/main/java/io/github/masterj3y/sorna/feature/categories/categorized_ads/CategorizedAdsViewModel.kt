package io.github.masterj3y.sorna.feature.categories.categorized_ads

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CategorizedAdsViewModel @ViewModelInject constructor(private val repository: AdsRepository): BaseViewModel() {

    private val getCategorizedAds = MutableLiveData<String>()
    @ExperimentalCoroutinesApi
    val categorizedAds: LiveData<List<Ad>> = getCategorizedAds.switchMap {
        loading()
        launchOnViewModelScope {
            repository.getAllAdsByCategory(it, {loading(false)}, ::error)
        }
    }

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getCategorizedAds(categoryId: String) {
        this.getCategorizedAds.value= categoryId
    }

    private fun loading(isLoading: Boolean = true) {
        this.loading.value = isLoading
    }

    private fun error(message: String) {
        loading(false)
        this.error.value = message
    }
}