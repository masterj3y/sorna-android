package io.github.masterj3y.sorna.feature.ad.ads

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.masterj3y.sorna.feature.ad.AdPicture

object AdPictureListTypeConverter {

    @JvmStatic
    @TypeConverter
    fun fromAdPicture(pics: AdPicture?): String? =
        pics?.let { Gson().toJson(it, object : TypeToken<AdPicture>() {}.type) }

    @JvmStatic
    @TypeConverter
    fun toAdPicture(json: String?): AdPicture? =
        json?.let { Gson().fromJson(it, object : TypeToken<AdPicture>() {}.type) }

    @JvmStatic
    @TypeConverter
    fun fromPics(pics: List<AdPicture>?): String? =
        pics?.let { Gson().toJson(it, object : TypeToken<List<AdPicture>>() {}.type) }

    @JvmStatic
    @TypeConverter
    fun toPics(json: String?): List<AdPicture>? =
        json?.let { Gson().fromJson(it, object : TypeToken<List<AdPicture>>() {}.type) }
}