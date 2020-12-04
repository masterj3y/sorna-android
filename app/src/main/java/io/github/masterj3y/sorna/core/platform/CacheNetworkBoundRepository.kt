package io.github.masterj3y.sorna.core.platform

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class CacheNetworkBoundRepository<RESULT, REQUEST>(
        private val onSuccess: () -> Unit,
        private val onError: (String) -> Unit
) {

    fun asFlow() = flow {

        emit(fetchFromLocal().first())
        val apiResponse = fetchFromRemote()
        val remoteData = apiResponse.body()
        if (apiResponse.isSuccessful && remoteData != null) {
            saveRemoteData(remoteData)
        } else {
            onError(apiResponse.message())
        }
        emitAll(fetchFromLocal())
        onSuccess()
    }.catch { e ->
        e.message?.let { onError(it) }
    }

    @WorkerThread
    abstract suspend fun saveRemoteData(response: REQUEST)

    @MainThread
    abstract fun fetchFromLocal(): Flow<RESULT>

    @WorkerThread
    abstract suspend fun fetchFromRemote(): Response<REQUEST>
}