package io.github.masterj3y.sorna.core.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

}