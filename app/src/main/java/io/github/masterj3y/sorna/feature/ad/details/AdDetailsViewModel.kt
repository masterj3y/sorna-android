package io.github.masterj3y.sorna.feature.ad.details

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import io.github.masterj3y.sorna.core.extension.call
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class AdDetailsViewModel @ViewModelInject constructor(private val repository: AdsRepository) : BaseViewModel() {

    val saveProgress = MutableLiveData<Boolean>()
    val isSaved = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    private val getAd = MutableLiveData<String>()
    @ExperimentalCoroutinesApi
    val ad : LiveData<Ad> = getAd.switchMap {
        launchOnViewModelScope { repository.getAdById(it) }
    }

    fun getAd(adId: String) {
        getAd.value = adId
    }

    @ExperimentalCoroutinesApi
    fun save(adId: String) {
        saveProgress()
        viewModelScope.launch {
            repository.save(adId, onSuccess = { saved(true) }, onError = ::error)
        }
    }

    @ExperimentalCoroutinesApi
    fun waste(adId: String) {
        saveProgress()
        viewModelScope.launch {
            repository.waste(adId, onSuccess = { saved(false) }, onError = ::error)
        }
    }

    fun call(view: View?, phoneNumber: String) {
        view?.context?.call(phoneNumber)
    }

    private fun saveProgress(isProgressing: Boolean = true) {
        this.saveProgress.value = isProgressing
    }

    private fun saved(isBookmarked: Boolean) {
        saveProgress(false)
        this.isSaved.value = true
    }

    private fun error(message: String) {
        saveProgress(false)
        this.error.value = message
    }
}