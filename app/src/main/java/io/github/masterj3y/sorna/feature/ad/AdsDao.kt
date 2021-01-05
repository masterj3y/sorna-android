package io.github.masterj3y.sorna.feature.ad

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AdsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg ad: Ad)

    @Query("SELECT * FROM Ad ORDER BY createdAt DESC")
    fun findAll(): Flow<List<Ad>>

    @Query("SELECT * FROM Ad WHERE categoryId = :categoryId ORDER BY createdAt DESC")
    fun findAllByCategory(categoryId: String): Flow<List<Ad>>

    @Query("SELECT * FROM ad WHERE ownedByUser = 1 ORDER BY createdAt DESC")
    fun findAllUserAds(): Flow<List<Ad>>

    @Query("SELECT * FROM Ad WHERE saved = 1 ORDER BY createdAt DESC")
    fun findAllSavedAds(): Flow<List<Ad>>

    @Query("SELECT * FROM Ad WHERE id = :adId")
    fun findById(adId: String): Flow<Ad>

    @Query("UPDATE Ad SET saved = 1 WHERE id = :adId")
    suspend fun update(adId: String)

    @Query("UPDATE Ad SET saved = 0 WHERE id = :adId")
    suspend fun waste(adId: String)

    @Query("DELETE FROM ad WHERE id = :adId")
    suspend fun delete(adId: String)

    @Query("DELETE FROM ad WHERE id NOT IN (SELECT id FROM ad WHERE id IN (:ids))")
    suspend fun deleteByNotExist(ids: String)

    @Transaction
    suspend fun update(vararg ads: Ad) {
        if (ads.isNotEmpty()) {
            val ids: String = ads.map { "${it.id}," }.toString().run { substring(1, length - 3) }
            deleteByNotExist(ids)
            insert(*ads)
        }
    }
}