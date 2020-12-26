package io.github.masterj3y.sorna.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.feature.ad.AdService
import io.github.masterj3y.sorna.feature.auth.AuthInterceptor
import io.github.masterj3y.sorna.feature.auth.AuthService
import io.github.masterj3y.sorna.feature.categories.CategoriesService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    const val BASE_URL = "http://192.168.1.103:8080/"

    @Provides
    @Singleton
    fun provideCategoriesService(retrofit: Retrofit): CategoriesService =
            retrofit.create(CategoriesService::class.java)

    @Provides
    @Singleton
    fun provideAdsService(retrofit: Retrofit): AdService =
            retrofit.create(AdService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
            retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun okHttpClient(appSession: AppSession): OkHttpClient {
        return OkHttpClient().newBuilder()
                .addInterceptor(AuthInterceptor(appSession))
                .build()
    }
}