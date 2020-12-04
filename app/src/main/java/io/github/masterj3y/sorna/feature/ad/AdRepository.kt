package io.github.masterj3y.sorna.feature.ad

import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AdRepository(private val service: AdService) {

    @ExperimentalCoroutinesApi
    suspend fun postAd(
            onSuccess: () -> Unit,
            onError: (String) -> Unit,
            title: RequestBody,
            description: RequestBody,
            phoneNumber: RequestBody,
            price: RequestBody,
            categoryId: RequestBody,
            vararg pics: MultipartBody.Part
    ) {

        try {
            val apiResponse = service.postAd(title, description, phoneNumber, price, categoryId, *pics)
            val body = apiResponse.body()
            if (apiResponse.isSuccessful && body != null) {
                onSuccess()
            } else {
                onError(apiResponse.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}