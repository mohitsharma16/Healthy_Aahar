package com.mohit.healthy_aahar.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val WEB_CLIENT_ID = "954079451159-9a131fqstkisntq9mrbq85obut65m8c3.apps.googleusercontent.com"  // 🔹 Replace this with your actual Client ID
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

//    @Provides
//    @Singleton
//    fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(WEB_CLIENT_ID)
//            .requestEmail()
//            .build()
//        return GoogleSignIn.getClient(context, gso)
//    }
}
