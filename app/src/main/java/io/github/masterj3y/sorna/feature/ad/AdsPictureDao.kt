package io.github.masterj3y.sorna.feature.ad

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdsPictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg adPicture: AdPicture)

    @Query("SELECT * FROM adpicture WHERE adId = :adId")
    fun findAllByAdId(adId: String): Flow<List<AdPicture>>
}