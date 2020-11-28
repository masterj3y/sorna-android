package io.github.masterj3y.sorna.feature.categories

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.github.masterj3y.sorna.core.platform.BaseEntity
import java.util.*

@Entity(tableName = "categories")
data class Category(
        @PrimaryKey
        @SerializedName("id")
        override val id: String,
        @SerializedName("title")
        val title: String
): BaseEntity<String>