package io.github.masterj3y.sorna.feature.ad

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class AdPicture(
        @PrimaryKey
        val id: String,
        @SerializedName("picUrl")
        val picUrl: String,
        @Expose
        var adId: String? = null
): Parcelable
