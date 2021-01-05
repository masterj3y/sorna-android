package io.github.masterj3y.sorna.feature.ad.user_ads

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class UserAdsViewModel @ViewModelInject constructor(private val repository: AdsRepository) : BaseViewModel() {

    private val getUserAds = MutableLiveData<Boolean>()

    @ExperimentalCoroutinesApi
    private val _userAds: MutableLiveData<List<Ad>> = getUserAds.switchMap {
        loading()
        launchOnViewModelScope {
            loading()
            repository.getAllUserAds({ loading(false) }, ::error)
        }
    } as MutableLiveData<List<Ad>>

    @ExperimentalCoroutinesApi
    val userAds: LiveData<List<Ad>>
        get() = _userAds

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getUserAds() {
        this.getUserAds.value = true
    }

    @ExperimentalCoroutinesApi
    fun delete(adId: String) {
        viewModelScope.launch { repository.delete(adId, {}, {}) }
        _userAds.value = _userAds.value?.toMutableList()?.apply {
            remove(userAds.value?.first { it.id == adId })
        }
    }

    private fun loading(loading: Boolean = true) {
        this.loading.postValue(loading)
    }

    private fun error(error: String) {
        loading(false)
        this.error.postValue(error)
    }
}