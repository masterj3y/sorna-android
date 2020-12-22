package io.github.masterj3y.sorna.feature.categories

import io.github.masterj3y.sorna.core.database.AppDatabase
import io.github.masterj3y.sorna.core.platform.CacheNetworkBoundRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val service: CategoriesService, private val appDatabase: AppDatabase) {

    private val dao: CategoriesDao get() = appDatabase.categoriesDao()

    @ExperimentalCoroutinesApi
    fun getAllCategories() = object : CacheNetworkBoundRepository<List<Category>, List<Category>>(
            onSuccess = {}, onError = {println(it) }
    ) {
        override suspend fun saveRemoteData(response: List<Category>) =
                dao.insert(*response.toTypedArray())

        override fun fetchFromLocal(): Flow<List<Category>> =
                dao.findAll()

        override suspend fun fetchFromRemote(): Response<List<Category>> =
                service.fetchAll()
    }.asFlow()

    fun getCachedCategories(): Flow<List<Category>> = dao.findAll()
}