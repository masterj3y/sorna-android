package io.github.masterj3y.sorna.feature.ad

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.masterj3y.sorna.feature.ad.Ad
import kotlinx.coroutines.flow.Flow

@Dao
interface AdsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg ad: Ad)

    @Query("SELECT * FROM Ad ORDER BY createdAt DESC")
    fun findAll(): Flow<List<Ad>>

    @Query("SELECT * FROM Ad WHERE saved = 1 ORDER BY createdAt DESC")
    fun findAllSavedAds(): Flow<List<Ad>>

    @Query("SELECT * FROM Ad WHERE id = :adId")
    fun findById(adId: String): Flow<Ad>

    @Query("UPDATE Ad SET saved = 1 WHERE id = :adId")
    suspend fun save(adId: String)

    @Query("UPDATE Ad SET saved = 0 WHERE id = :adId")
    suspend fun waste(adId: String)
}