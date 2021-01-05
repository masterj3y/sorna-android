package io.github.masterj3y.sorna.feature.ad.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SearchAdsViewModel @ViewModelInject constructor(private val repository: AdsRepository) : BaseViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    private val keyword = MutableLiveData<String>()

    @ExperimentalCoroutinesApi
    private val _searchResultAds = keyword.switchMap {
        loading()
        launchOnViewModelScope {
            repository.searchAds(it, onSuccess = { loading(false) }, onError = ::error)
        }
    } as MutableLiveData<List<Ad>>

    @ExperimentalCoroutinesApi
    val searchResultAds: LiveData<List<Ad>> get() = _searchResultAds

    @ExperimentalCoroutinesApi
    fun search(s: CharSequence, start: Int = 0, before: Int = 0, count: Int = 0) {
        if (s.isNotBlank())
            keyword.value = s.toString()
        else
            this._searchResultAds.value = listOf()
    }

    private fun loading(isLoading: Boolean = true) {
        this.loading.value = isLoading
    }

    private fun error(message: String) {
        loading(false)
        this.error.value = message
    }
}