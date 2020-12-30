package io.github.masterj3y.sorna.feature.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CategoriesViewModel @ViewModelInject constructor(private val repository: CategoriesRepository) : BaseViewModel() {

    private val getCategories = MutableLiveData(false)

    @ExperimentalCoroutinesApi
    val categories: LiveData<List<Category>> = getCategories.switchMap {
        loading()
        launchOnViewModelScope {
            repository.getAllCategories(onSuccess = { loading(false) }, onError = { error(it) })
        }
    }

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getCategories() {
        this.getCategories.value = true
    }

    private fun loading(isLoading: Boolean = true) {
        this.loading.value = isLoading
    }

    private fun error(message: String) {
        loading(false)
        this.error.value = message
    }
}