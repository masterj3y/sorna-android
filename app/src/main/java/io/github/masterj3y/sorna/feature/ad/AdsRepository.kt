package io.github.masterj3y.sorna.feature.ad

import io.github.masterj3y.sorna.core.database.AppDatabase
import io.github.masterj3y.sorna.core.platform.CacheNetworkBoundRepository
import io.github.masterj3y.sorna.core.platform.NetworkBoundRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AdsRepository @Inject constructor(private val service: AdService, private val appDatabase: AppDatabase) {

    private val adsDao: AdsDao get() = appDatabase.adsDao()
    private val adPicturesDao: AdsPictureDao get() = appDatabase.adPicturesDao()

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
    ) = networkRequest(
            onSuccess = {
                saveAdsToLocal(listOf(it))
                onSuccess()
            },
            onError = { onError(it) },
            request = {
                service.postAd(title, description, phoneNumber, price, categoryId, *pics)
            }
    )

    @ExperimentalCoroutinesApi
    fun getAllAds(
            onSuccess: () -> Unit,
            onError: (String) -> Unit
    ): Flow<List<Ad>> =
            object : CacheNetworkBoundRepository<List<Ad>, List<Ad>>(onSuccess, onError) {

                override suspend fun saveRemoteData(response: List<Ad>) = updateLocalAds(response)

                override fun fetchFromLocal(): Flow<List<Ad>> = fetchAdsFromLocal(adsDao.findAll())

                override suspend fun fetchFromRemote(): Response<List<Ad>> =
                        service.fetchAll()
            }.asFlow()

    @ExperimentalCoroutinesApi
    fun getAllAdsByCategory(
            categoryId: String,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
    ): Flow<List<Ad>> =
            object : CacheNetworkBoundRepository<List<Ad>, List<Ad>>(onSuccess, onError) {

                override suspend fun saveRemoteData(response: List<Ad>) = updateLocalAds(response)

                override fun fetchFromLocal(): Flow<List<Ad>> =
                        fetchAdsFromLocal(adsDao.findAllByCategory(categoryId))

                override suspend fun fetchFromRemote(): Response<List<Ad>> =
                        service.fetchAllByCategory(categoryId)
            }.asFlow()

    @ExperimentalCoroutinesApi
    fun getAllUserAds(
            onSuccess: () -> Unit,
            onError: (String) -> Unit
    ): Flow<List<Ad>> =
            object : NetworkBoundRepository<List<Ad>>(onSuccess, onError) {
                override suspend fun fetchFromRemote(): Response<List<Ad>> =
                        service.fetchAllUserAds()
            }.asFlow()

    @ExperimentalCoroutinesApi
    fun getAllSavedAds(
            onSuccess: () -> Unit,
            onError: (String) -> Unit
    ): Flow<List<Ad>> =
            object : CacheNetworkBoundRepository<List<Ad>, List<Ad>>(onSuccess, onError) {

                override suspend fun saveRemoteData(response: List<Ad>) = updateLocalAds(response)

                override fun fetchFromLocal(): Flow<List<Ad>> = fetchAdsFromLocal(adsDao.findAllSavedAds())

                override suspend fun fetchFromRemote(): Response<List<Ad>> =
                        service.fetchAllSavedAds()
            }.asFlow()

    @ExperimentalCoroutinesApi
    fun getAdById(adId: String): Flow<Ad> = flow {
        try {
            val adFlow = adsDao.findById(adId).onEach {
                val adPictures = adPicturesDao.findAllByAdId(it.id).first()
                it.pics = adPictures
            }
            emitAll(adFlow)
        } catch (e: IOException) {
        }
    }

    @ExperimentalCoroutinesApi
    fun searchAds(keyword: String, onSuccess:  () -> Unit, onError: (String) -> Unit): Flow<List<Ad>> =
            object : NetworkBoundRepository<List<Ad>>(onSuccess, onError) {
                override suspend fun fetchFromRemote(): Response<List<Ad>> = service.searchAds(keyword)
            }.asFlow()

    private suspend fun saveAdsToLocal(ads: List<Ad>) {
        ads.forEach { ad ->
            ad.pics?.let { pics ->
                pics.forEach { pic -> pic.adId = ad.id }
                adPicturesDao.insert(*pics.toTypedArray())
            }
        }
        adsDao.insert(*ads.toTypedArray())
    }

    private suspend fun updateLocalAds(ads: List<Ad>) {
        ads.forEach { ad ->
            ad.pics?.let { pics ->
                pics.forEach { pic -> pic.adId = ad.id }
                adPicturesDao.insert(*pics.toTypedArray())
            }
        }
        adsDao.update(*ads.toTypedArray())
    }

    @ExperimentalCoroutinesApi
    suspend fun save(adId: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) =
            networkRequest(
                    onSuccess = {
                        adsDao.update(adId)
                        onSuccess(it)
                    },
                    onError = { onError(it) },
                    request = { service.save(adId) }
            )

    @ExperimentalCoroutinesApi
    suspend fun waste(adId: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) =
            networkRequest(
                    onSuccess = {
                        adsDao.waste(adId)
                        onSuccess(it)
                    },
                    onError = { onError(it) },
                    request = { service.waste(adId) }
            )

    suspend fun delete(adId: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) =
            networkRequest(
                    onSuccess = {
                        adsDao.delete(adId)
                        adPicturesDao.delete(adId)
                        onSuccess(it)
                    },
                    onError = { onError(it) },
                    request = { service.delete(adId) }
            )

    @ExperimentalCoroutinesApi
    private fun fetchAdsFromLocal(request: Flow<List<Ad>>): Flow<List<Ad>> = flow {
        val adsFlow = request.onEach { ads ->
            ads.forEach {
                val adId = it.id
                val adPictures = adPicturesDao.findAllByAdId(adId).first()
                it.pics = adPictures
            }
        }
        emitAll(adsFlow)
    }

    private suspend fun <T> networkRequest(onSuccess: suspend (T) -> Unit, onError: suspend (String) -> Unit, request: suspend () -> Response<T>) {
        io {

            try {
                val response = request()
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    main { onSuccess(responseBody) }
                } else {
                    main { onError(response.message()) }
                }
            } catch (e: IOException) {
                e.message?.let { main { onError(it) } }
            }
        }
    }

    private suspend fun main(func: suspend () -> Unit) = withContext(Main) { func() }
    private suspend fun io(func: suspend () -> Unit) = withContext(IO) { func() }
}