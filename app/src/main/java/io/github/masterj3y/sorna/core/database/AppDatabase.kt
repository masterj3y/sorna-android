package io.github.masterj3y.sorna.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.masterj3y.sorna.feature.categories.CategoriesDao
import io.github.masterj3y.sorna.feature.categories.Category


@Database(
    entities = [Category::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriesDao(): CategoriesDao
}