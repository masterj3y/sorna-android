package io.github.masterj3y.sorna.core.platform

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
abstract class CacheNetworkBoundRepository<RESULT, REQUEST>(
        private val onSuccess: () -> Unit,
        private val onError: (String) -> Unit
) {

    fun asFlow() = flow {
        try {
            val apiResponse = fetchFromRemote()
            val remoteData = apiResponse.body()
            if (apiResponse.isSuccessful && remoteData != null) {
                onSuccess()
                saveRemoteData(remoteData)
                emitAll(fetchFromLocal())
            } else {
                onError(apiResponse.message())
            }
        } catch (e: IOException) {
            e.message?.let {
                onError(it)
                e.printStackTrace()
            }
        }
    }

    @WorkerThread
    abstract suspend fun saveRemoteData(response: REQUEST)

    @MainThread
    abstract fun fetchFromLocal(): Flow<RESULT>

    @WorkerThread
    abstract suspend fun fetchFromRemote(): Response<REQUEST>
}