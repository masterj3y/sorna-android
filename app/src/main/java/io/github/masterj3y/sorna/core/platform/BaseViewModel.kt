package io.github.masterj3y.sorna.core.platform

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel : ViewModel() {

    inline fun <T> launchOnViewModelScope(crossinline block: suspend () -> Flow<T>): LiveData<T> {
        return liveData(viewModelScope.coroutineContext + IO) {
            emitSource(block().asLiveData())
        }
    }
}