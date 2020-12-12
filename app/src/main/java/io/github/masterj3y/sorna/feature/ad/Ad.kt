package io.github.masterj3y.sorna.feature.ad

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.github.masterj3y.sorna.core.platform.BaseEntity
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Ad(
        @PrimaryKey
        @SerializedName("id")
        override val id: String,
        @SerializedName("categoryId")
        val categoryId: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("price")
        val price: Long,
        @SerializedName("createdAt")
        val createdAt: Long,
        @SerializedName("saved")
        val saved: Boolean
) : BaseEntity<String>, Parcelable {

    @Ignore
    @SerializedName("pics")
    var pics: List<AdPicture>? = null
}
