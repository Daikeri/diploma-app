package com.example.movie_rec_sys.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrimaryRecRepository(
    private val dataSource: PrimaryRecDataSource
) {
    private lateinit var fetchesIDs: MutableMap<String, MutableMap<String, Any?>>

    suspend fun fetchIDs(): MutableMap<String, MutableMap<String, Any?>> {
        val networkResult = withContext(Dispatchers.IO) {
            dataSource.fetchIDs()
        }
        this.fetchesIDs = networkResult
        return this.fetchesIDs
    }

    suspend fun sendFeedback(map: MutableMap<String, MutableMap<String, Any?>>): Boolean {
        return withContext(Dispatchers.IO) {
            dataSource.sendFeedback(map)
        }
    }
}