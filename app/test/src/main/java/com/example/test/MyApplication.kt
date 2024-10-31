package com.example.test

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.auth.AuthModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication: Application()