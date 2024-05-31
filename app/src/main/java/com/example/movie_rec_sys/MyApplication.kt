package com.example.movie_rec_sys

import android.app.Application
import com.example.movie_rec_sys.dependcy.AppContainer
import com.google.firebase.FirebaseApp

class MyApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appContainer = AppContainer(this)
    }
}