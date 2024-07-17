package com.example.movie_rec_sys.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ItemRepository(
    private val dataSource: ItemRemoteDataSource,
    private val firestoreRS: FirestoreRemoteSource
) {
    private val sharedItems: MutableMap<String, Movie> = mutableMapOf()

    private val recommendationsStruct: MutableMap<String,
            MutableMap<String, Pair<RecommendationDoc, Movie?>>> = mutableMapOf()

    private val userCollectionStruct: MutableMap<String, Movie> = mutableMapOf()

    val recScreenData = flow<MutableMap<String,
    MutableMap<String, Pair<RecommendationDoc, Movie?>>>> {
        firestoreRS.recommendation.collect { updates ->
            updates.forEach { (actionFlag, docId, docContent) ->
                when(actionFlag) {
                    "ADDED" -> {
                        val category = recommendationsStruct.getOrPut(docContent.category) { mutableMapOf() }
                        category[docId] = Pair(docContent, null)
                    }
                }
            }
            emit(recommendationsStruct)

            recommendationsStruct.keys.forEach { category ->
                recommendationsStruct[category]!!.forEach { item ->
                    val networkResult = updatedGetUserItem(itemID = item.value.first.itemId)
                    sharedItems[item.value.first.itemId] = networkResult
                    recommendationsStruct[category]?.set(item.key, Pair(item.value.first, networkResult))
                    emit(recommendationsStruct)
                }
            }
        }

    }.flowOn(Dispatchers.IO)


    suspend fun getUserItem(docID: String, itemID: String): Movie {
        val networkResult = withContext(Dispatchers.IO) {
            dataSource.fetchItem(itemID)
        }
        sharedItems[docID] = networkResult
        return networkResult
    }

    private suspend fun updatedGetUserItem(itemID: String): Movie {
        return withContext(Dispatchers.IO) {
            dataSource.fetchItem(itemID)
        }
    }
    
    suspend fun withoutCash(itemID: String): Movie {
        val networkResult = withContext(Dispatchers.IO) {
            dataSource.fetchItem(itemID)
        }
        return networkResult
    }
}