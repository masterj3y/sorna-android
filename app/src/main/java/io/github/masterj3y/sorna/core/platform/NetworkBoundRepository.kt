package io.github.masterj3y.sorna.core.platform

import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<REQUEST>(
    private val onSuccess: () -> Unit,
    private val onError: (String) -> Unit
) {

    fun asFlow() = flow<REQUEST> {

        try {
            val apiResponse = fetchFromRemote()
            val body = apiResponse.body()
            if (apiResponse.isSuccessful && body != null) {
                onSuccess()
                emit(body)
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
    abstract suspend fun fetchFromRemote(): Response<REQUEST>
}