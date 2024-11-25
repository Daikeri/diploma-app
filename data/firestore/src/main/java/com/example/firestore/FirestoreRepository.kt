package com.example.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class FirestoreRepository @Inject constructor(
    private val firestoreRDS: FirebaseFirestore
) {
    fun recommendation(firebaseUserID: String):Flow<List<Triple<String, String, RecommendationDoc>>>
    = callbackFlow {
        val request = firestoreRDS.collection("recommendation")
            .whereEqualTo("user_id", firebaseUserID)
            .orderBy("relevance_index", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, fireStoreExcept ->
                if (fireStoreExcept != null) {
                    return@addSnapshotListener
                }
                val result = snapshot!!.documentChanges.map {
                    val hash = it.document.data
                    val actionFlag = it.type.toString()
                    val docId = it.document.id
                    val docContent = RecommendationDoc(
                        category = hash["category"] as String,
                        itemId = hash["source_item_id"] as String,
                        marked = hash["marked"] as Boolean,
                        viewed = hash["viewed"] as Boolean,
                        rated = hash["rated"] as? Int,
                        relevanceIndex = (hash["relevance_index"] as Long).toInt(),
                    )
                    Triple(actionFlag, docId, docContent)
                }
                trySend(result)
            }

        awaitClose { request.remove() }

    }.flowOn(Dispatchers.IO)
}

data class RecommendationDoc(
    val category: String,
    val itemId: String,
    val marked: Boolean,
    val viewed: Boolean,
    val rated: Int?,
    val relevanceIndex: Int,
)

/*

data class RecommendationDocContent(
    val category: String,
    val itemId: String,
    val marked: Boolean,
    val viewed: Boolean,
    val rated: Int?,
    val relevanceIndex: Int,
)

data class RecommendationDoc(
    val docID: String
    val content: RecommendationDocContent
    val actionFlag: String
)


 */