package com.example.auth

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    init {
        Log.e("AuthModule created", "")
    }
    @Provides
    @Singleton
    fun provideFirebaseAuth(@ApplicationContext applicationContext: Context): FirebaseAuth {
        FirebaseApp.initializeApp(applicationContext)
        return FirebaseAuth.getInstance()
    }
}
