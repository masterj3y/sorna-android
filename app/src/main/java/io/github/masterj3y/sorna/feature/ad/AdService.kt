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

    @GET("ad/category/{id}")
    suspend fun fetchAllByCategory(@Path("id") id: String): Response<List<Ad>>

    @GET("ad/user-ads")
    suspend fun fetchAllUserAds(): Response<List<Ad>>

    @GET("ad/user-saved-ads")
    suspend fun fetchAllSavedAds(): Response<List<Ad>>

    @GET("ad/search/{keyword}")
    suspend fun searchAds(@Path("keyword") keyword: String): Response<List<Ad>>

    @PUT("ad/save/{id}")
    suspend fun save(@Path("id") adId: String): Response<String>

    @PUT("ad/waste/{id}")
    suspend fun waste(@Path("id") adId: String): Response<String>
}