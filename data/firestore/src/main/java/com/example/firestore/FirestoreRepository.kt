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
    val firestoreRDS: FirebaseFirestore
) {
    private val cashedDoc: MutableMap<String, RecDocContent> = mutableMapOf()

    fun recommendation(firebaseUserID: String): Flow<List<RecommendationDoc>>
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
                    val docContent = RecDocContent(
                        category = hash["category"] as String,
                        downloadResID = hash["source_item_id"] as String,
                        marked = hash["marked"] as Boolean,
                        viewed = hash["viewed"] as Boolean,
                        rated = hash["rated"] as? Int,
                        relevanceIndex = (hash["relevance_index"] as Long).toInt(),
                    )
                    cashedDoc[docId] = docContent

                    RecommendationDoc(
                        actionFlag = actionFlag,
                        docID = docId,
                        category = docContent.category,
                        downloadResID = docContent.downloadResID,
                        relevanceIndex = docContent.relevanceIndex
                    )
                }
                trySend(result)
            }

        awaitClose { request.remove() }

    }.flowOn(Dispatchers.IO)
}


data class RecDocContent(
    val category: String,
    val downloadResID: String,
    val marked: Boolean,
    val viewed: Boolean,
    val rated: Int?,
    val relevanceIndex: Int,
)

data class RecommendationDoc(
    val actionFlag: String,
    val docID: String,
    val category: String,
    val downloadResID: String,
    val relevanceIndex: Int,
)

