package com.example.movie_rec_sys.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(
    private val dataSource: ItemRemoteDataSource
) {
    private var userItems: MutableMap<String, Movie> = mutableMapOf()

    suspend fun getUserItem(docID: String, itemID: String): Movie {
        val networkResult = withContext(Dispatchers.IO) {
            dataSource.fetchItem(itemID)
        }
        userItems[docID] = networkResult
        return networkResult
    }

    suspend fun withoutCash(itemID: String): Movie {
        val networkResult = withContext(Dispatchers.IO) {
            dataSource.fetchItem(itemID)
        }
        return networkResult
    }

}