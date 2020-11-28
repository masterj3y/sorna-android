package io.github.masterj3y.sorna.feature.categories

import retrofit2.Response
import retrofit2.http.GET

interface CategoriesService {

    @GET("category/")
    suspend fun fetchAll(): Response<List<Category>>
}