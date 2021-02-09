package io.github.masterj3y.sorna.core.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.masterj3y.sorna.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context,
                                  googleSignInOptions: GoogleSignInOptions): GoogleSignInClient =
            GoogleSignIn.getClient(context, googleSignInOptions)

    @Provides
    @Singleton
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(BuildConfig.GOOGLE_REQUEST_ID_TOKEN)
                    .requestEmail()
                    .build()
}