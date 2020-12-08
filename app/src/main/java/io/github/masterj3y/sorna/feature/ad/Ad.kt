package io.github.masterj3y.sorna.feature.ad

import androidx.room.*
import com.google.gson.annotations.SerializedName
import io.github.masterj3y.sorna.core.platform.BaseEntity

@Entity
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
    val createdAt: Long
): BaseEntity<String> {

    @Ignore
    @SerializedName("pics")
    var pics: List<AdPicture>? = null
}
