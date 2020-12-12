package io.github.masterj3y.sorna.feature.ad

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @PUT("ad/save/{id}")
    suspend fun save(@Path("id") adId: String): Response<String>

    @PUT("ad/waste/{id}")
    suspend fun waste(@Path("id") adId: String): Response<String>
}