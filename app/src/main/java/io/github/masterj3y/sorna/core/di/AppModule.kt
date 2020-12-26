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
                    .requestIdToken("809003685834-bqkuodq77dkn38iur2u4ii1ocggrbf8o.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
}