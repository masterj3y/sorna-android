package io.github.masterj3y.sorna.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.masterj3y.sorna.feature.auth.AuthRepository
import io.github.masterj3y.sorna.feature.auth.AuthService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(authService: AuthService): AuthRepository =
            AuthRepository(authService)
}