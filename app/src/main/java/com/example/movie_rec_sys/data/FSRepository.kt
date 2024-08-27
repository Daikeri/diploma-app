package com.example.movie_rec_sys.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FSRepository(
    private val dataSource: ItemRemoteDataSource,
    private val firestoreRS: FirestoreRemoteSource
) {
    private val recommendationsStruct: MutableMap<String,
            MutableMap<String, Pair<RecommendationDoc, Movie?>>> = mutableMapOf()

    private val userCollectionStruct: MutableMap<String,
            MutableMap<String, Pair<UserCollectionDoc, Movie?>>> = mutableMapOf(
                "views" to mutableMapOf(),
                "marked" to mutableMapOf()
            )

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
                    val targetItem = dataSource.getItem(item.value.first.itemId)
                    recommendationsStruct[category]?.set(item.key, Pair(item.value.first, targetItem))
                    delay(500)
                    emit(recommendationsStruct)
                }
            }
        }

    }.flowOn(Dispatchers.IO)

    val userCollectionData = flow<MutableMap<String,
    MutableMap<String, Pair<UserCollectionDoc, Movie?>>>> {
        firestoreRS.usersCollection.collect { updates ->
            updates.forEach { (actionFlag, docID, docContent) ->
                when(actionFlag) {
                    "ADDED" -> {
                        if (docContent.viewed || docContent.rated!= null)
                            userCollectionStruct["views"]!![docID] = Pair(docContent, null)
                        if (docContent.marked)
                            userCollectionStruct["marked"]!![docID] = Pair(docContent, null)
                    }
                }
            }
            emit(userCollectionStruct)

            userCollectionStruct.keys.forEach { collection ->
                userCollectionStruct[collection]!!.forEach { item ->
                    val targetItem = dataSource.getItem(item.value.first.itemId)
                    userCollectionStruct[collection]?.set(item.key, Pair(item.value.first, targetItem))
                    delay(500)
                    emit(userCollectionStruct)
                }
            }
        }
    }

    fun getItem(category: String, document: String): Pair<RecommendationDoc, Movie?>? {
        return recommendationsStruct[category]!![document]
    }

    private suspend fun updatedGetUserItem(itemID: String): Movie {
        return dataSource.getItem(itemID)
    }
    
    suspend fun withoutCash(itemID: String): Movie {
        val networkResult = withContext(Dispatchers.IO) {
            dataSource.downloadItem(itemID)
        }
        return networkResult
    }
}