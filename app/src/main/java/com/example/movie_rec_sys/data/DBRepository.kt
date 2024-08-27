package com.example.movie_rec_sys.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DBRepository(
    private val dbInstance: AppDB,
    private val itemRDS: ItemRemoteDataSource
) {
    private val dbDao = dbInstance.moviesDao()

    private val searchStruct = mutableMapOf<String, Pair<MovieFromDB, Movie?>>()

    fun findMovieByTitle(title: String) = flow<Map<String, Pair<MovieFromDB, Movie?>>> {
        searchStruct.clear()

        dbDao
            .findMovieByTitle(title)
            .map { shortMovie ->
                searchStruct[shortMovie.externalId] = Pair(shortMovie, null)
            }
        emit(searchStruct)

        searchStruct.forEach { entry ->
            val networkResult = itemRDS.getItem(entry.key)
            searchStruct[entry.key] = Pair(entry.value.first, networkResult)
            emit(searchStruct)
        }

        Log.e("SEARCH EMIT", "query=${title}|$searchStruct")
    }.flowOn(Dispatchers.IO)
}