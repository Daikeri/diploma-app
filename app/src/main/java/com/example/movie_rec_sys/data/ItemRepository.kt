package com.example.movie_rec_sys.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(
    private val dataSource: ItemRemoteDataSource
) {
    private var userItems: MutableList<Movie> = mutableListOf()

    suspend fun getUserItems(ids: List<String>): List<Movie> {
        val networkResult = withContext(Dispatchers.IO) {
            val finalList = mutableListOf<Movie>()
            ids.forEach {
                finalList.add(dataSource.fetchItem(it))
            }
            finalList
        }
        this.userItems = networkResult
        return this.userItems
    }

    suspend fun getUserItem(id: String): Movie {
        val requestedItem = userItems.find { it.externalId == id }
        val isExist: Pair<Boolean, Movie?> = ((requestedItem != null) to requestedItem)

        return if (isExist.first)
            isExist.second!!
        else {
            val networkResult = withContext(Dispatchers.IO) {
                dataSource.fetchItem(id)
            }
            this.userItems.add(networkResult)
            Log.e("MOVIE DOWNLOAD" , networkResult.externalId)
            networkResult
        }
    }
}