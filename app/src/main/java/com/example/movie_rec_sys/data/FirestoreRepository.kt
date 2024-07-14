package com.example.movie_rec_sys.data

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepository(
    private val fireStore: FirebaseFirestore,
    var currentUser: FirebaseUser?
) {
    suspend fun setNewUser(newVal: FirebaseUser?) {
        this.currentUser = newVal
    }

    val recommendation:Flow<List<Triple<String, String, FirestoreDoc>>> = callbackFlow {
        val request = fireStore.collection("recommendation")
            .whereEqualTo("user_id", currentUser?.uid)
            .orderBy("relevance_index", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, fireStoreExcept ->
                if (fireStoreExcept != null) {
                    return@addSnapshotListener
                }
                trySend(
                    snapshot!!.documentChanges.map {
                        val hash = it.document.data
                        val actionFlag = it.type.toString()
                        val docId = it.document.id
                        val docContent = FirestoreDoc(
                            category = hash["category"] as String,
                            itemId = hash["source_item_id"] as String,
                            marked = hash["marked"] as Boolean,
                            viewed = hash["viewed"] as Boolean,
                            rated = hash["rated"] as? Int,
                            relevanceIndex = (hash["relevance_index"] as Long).toInt(),
                        )
                        Triple(actionFlag, docId, docContent)
                    }
                )
            }

        awaitClose { request.remove() }

    }.flowOn(Dispatchers.IO)


    suspend fun uploadUpdates(
        collectionName: String="shared_pool",
        hash: MutableMap<String, Any?>
    ) {
        withContext(Dispatchers.IO) {
            fireStore.collection(collectionName)
                .add(hash)
                .addOnSuccessListener {
                    fireStore.waitForPendingWrites()
                    Log.d("Firestore Repos", "Data uploaded successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore Repos", "Error uploading data", e)
                }
                .await()
        }
    }

    suspend fun uploadMetaData(
        hash: MutableMap<String, Any?>,
        collectionName: String="users_profiles",
    ) {
        hash["user_id"] = currentUser!!.uid
        withContext(Dispatchers.IO) {
            fireStore.collection(collectionName)
                .add(hash)
                .addOnSuccessListener {
                    fireStore.waitForPendingWrites()
                    Log.d("Firestore Repos", "Data uploaded successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore Repos", "Error uploading data", e)
                }
                .await()
        }
    }
}

data class FirestoreDoc(
    val category: String,
    val itemId: String,
    val marked: Boolean,
    val viewed: Boolean,
    val rated: Int?,
    val relevanceIndex: Int,
)

