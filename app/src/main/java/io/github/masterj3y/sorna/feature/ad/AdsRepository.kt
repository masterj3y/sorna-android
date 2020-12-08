package io.github.masterj3y.sorna.feature.ad

import io.github.masterj3y.sorna.core.database.AppDatabase
import io.github.masterj3y.sorna.core.platform.CacheNetworkBoundRepository
import io.github.masterj3y.sorna.feature.ad.user_ads.AdsDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class AdsRepository(private val service: AdService, private val appDatabase: AppDatabase) {

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
    suspend fun getAllAds(
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
    private fun fetchAdsFromLocal(): Flow<List<Ad>> = flow {
        val ads = adsDao.findAll().first()
        for (i in ads.indices) {
            val adId = ads[i].id
            val adPictures = adPicturesDao.findAllByAdId(adId).first()
            ads[i].pics = adPictures
        }
        emit(ads)
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
}