package io.github.masterj3y.sorna.feature.ad

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class AdPicture(
        @PrimaryKey
        val id: String,
        @SerializedName("picUrl")
        val picUrl: String,
        @Expose
        var adId: String? = null
)
