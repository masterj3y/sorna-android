package io.github.masterj3y.sorna.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdPicture
import io.github.masterj3y.sorna.feature.ad.AdsPictureDao
import io.github.masterj3y.sorna.feature.ad.user_ads.AdsDao
import io.github.masterj3y.sorna.feature.categories.CategoriesDao
import io.github.masterj3y.sorna.feature.categories.Category


@Database(
    entities = [
        Ad::class,
        AdPicture::class,
        Category::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun adsDao(): AdsDao
    abstract fun adPicturesDao(): AdsPictureDao
    abstract fun categoriesDao(): CategoriesDao
}