package io.github.masterj3y.sorna.feature.ad

import io.github.masterj3y.sorna.core.database.AppDatabase
import io.github.masterj3y.sorna.core.platform.CacheNetworkBoundRepository
import io.github.masterj3y.sorna.core.platform.NetworkBoundRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
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
    ) {

        try {
            val apiResponse = service.postAd(
                    title,
                    description,
                    phoneNumber,
                    price,
                    categoryId,
                    *pics
            )
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

    @ExperimentalCoroutinesApi
    fun getAllAds(
            onSuccess: () -> Unit,
            onError: (String) -> Unit
    ): Flow<List<Ad>> =
            object : CacheNetworkBoundRepository<List<Ad>, List<Ad>>(onSuccess, onError) {

                override suspend fun saveRemoteData(response: List<Ad>) = saveAdsInDB(response)

                override fun fetchFromLocal(): Flow<List<Ad>> = fetchAdsFromLocal()

                override suspend fun fetchFromRemote(): Response<List<Ad>> =
                        service.fetchAll()
            }.asFlow()

    @ExperimentalCoroutinesApi
    fun getAdById(adId: String): Flow<Ad> = flow {
        val adFlow = adsDao.findById(adId).onEach {
            val adPictures = adPicturesDao.findAllByAdId(it.id).first()
            it.pics = adPictures
        }
        emitAll(adFlow)
    }

    @ExperimentalCoroutinesApi
    fun searchAds(keyword: String, onSuccess: suspend () -> Unit, onError: suspend (String) -> Unit): Flow<List<Ad>> =
        object : NetworkBoundRepository<List<Ad>>(onSuccess, onError) {
            override suspend fun fetchFromRemote(): Response<List<Ad>> = service.searchAds(keyword)
        }.asFlow()

    @ExperimentalCoroutinesApi
    private fun fetchAdsFromLocal(): Flow<List<Ad>> = flow {
        val adsFlow = adsDao.findAll().onEach { ads ->
            ads.forEach {
                val adId = it.id
                val adPictures = adPicturesDao.findAllByAdId(adId).first()
                it.pics = adPictures
            }
        }
        emitAll(adsFlow)
    }

    suspend fun saveAdsInDB(ads: List<Ad>) {
        ads.forEach { ad ->
            ad.pics?.let { pics ->
                pics.forEach { pic -> pic.adId = ad.id }
                adPicturesDao.insert(*pics.toTypedArray())
            }
        }
        adsDao.insert(*ads.toTypedArray())
    }

    @ExperimentalCoroutinesApi
    suspend fun save(adId: String, onSuccess: suspend () -> Unit, onError: suspend (String) -> Unit) {
        object : NetworkBoundRepository<String>(
                onSuccess = {
                    adsDao.save(adId)
                    onSuccess()
                },
                onError = onError
        ) {
            override suspend fun fetchFromRemote(): Response<String> = service.save(adId)
        }.asFlow().first()
    }

    @ExperimentalCoroutinesApi
    suspend fun waste(adId: String, onSuccess: suspend () -> Unit, onError: suspend (String) -> Unit) {
        object : NetworkBoundRepository<String>(
                onSuccess = {
                    adsDao.waste(adId)
                    onSuccess()
                },
                onError = onError
        ) {
            override suspend fun fetchFromRemote(): Response<String> = service.waste(adId)
        }.asFlow().first()
    }
}