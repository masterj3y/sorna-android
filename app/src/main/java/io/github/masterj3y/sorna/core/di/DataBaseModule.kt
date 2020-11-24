package io.github.masterj3y.sorna.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataBaseModule {

//    @Provides
//    @Singleton
//    fun provideDatabase(context: Application): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, "db")
//            .fallbackToDestructiveMigration()
//            .build()
//    }
}