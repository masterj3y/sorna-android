package io.github.masterj3y.sorna.feature.ad.user_ads

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

    @Query("SELECT * FROM Ad ORDER BY createdAt")
    fun findAll(): Flow<List<Ad>>
}