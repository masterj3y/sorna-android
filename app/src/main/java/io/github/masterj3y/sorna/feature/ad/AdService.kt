package io.github.masterj3y.sorna.feature.ad

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AdService {

    @POST("ad/")
    @Multipart
    suspend fun postAd(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("price") price: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part vararg pics: MultipartBody.Part
    ): Response<String>

    @GET("ad/")
    suspend fun fetchAll(): Response<List<Ad>>
}