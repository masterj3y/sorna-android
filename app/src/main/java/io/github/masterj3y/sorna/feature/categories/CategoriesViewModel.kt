package io.github.masterj3y.sorna.feature.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CategoriesViewModel @ViewModelInject constructor(private val repository: CategoriesRepository) : BaseViewModel() {

    @ExperimentalCoroutinesApi
    val categories: LiveData<List<Category>> = launchOnViewModelScope {
        repository.getAllCategories()
    }
}