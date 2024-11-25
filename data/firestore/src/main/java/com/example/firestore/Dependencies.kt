package com.example.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.PersistentCacheSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Module
@InstallIn(SingletonComponent::class)
class FirestoreModule {
    init {
        Log.e("FirestoreModule created", "")
    }

    @Provides
    fun provideFirestore(): FirebaseFirestore {
        val instance = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .setSizeBytes(1)
                    .build()
            )
            .build()
        instance.firestoreSettings = settings
        return instance
    }
}