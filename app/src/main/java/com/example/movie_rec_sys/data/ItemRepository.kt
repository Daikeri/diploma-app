package com.example.movie_rec_sys.data

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
}