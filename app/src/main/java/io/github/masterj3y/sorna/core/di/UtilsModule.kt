package io.github.masterj3y.sorna.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.core.utils.SharedPref
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object UtilsModule {

    @Singleton
    @Provides
    fun provideAppSession(sharedPref: SharedPref): AppSession = AppSession(sharedPref)

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Application): SharedPref = SharedPref(context)
}