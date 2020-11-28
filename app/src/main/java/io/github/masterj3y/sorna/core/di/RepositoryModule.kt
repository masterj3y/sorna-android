package io.github.masterj3y.sorna.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.masterj3y.sorna.core.database.AppDatabase
import io.github.masterj3y.sorna.feature.auth.AuthRepository
import io.github.masterj3y.sorna.feature.auth.AuthService
import io.github.masterj3y.sorna.feature.categories.CategoriesRepository
import io.github.masterj3y.sorna.feature.categories.CategoriesService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCategoriesRepository(categoriesService: CategoriesService, appDatabase: AppDatabase): CategoriesRepository =
            CategoriesRepository(categoriesService, appDatabase)

    @Singleton
    @Provides
    fun provideAuthRepository(authService: AuthService): AuthRepository =
            AuthRepository(authService)
}