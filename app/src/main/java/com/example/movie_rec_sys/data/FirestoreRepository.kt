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
    private var currentUser: FirebaseUser?
) {
    suspend fun setNewUser(newVal: FirebaseUser?) {
        this.currentUser = newVal
    }

    val recommendation:Flow<List<MutableMap<String, Any?>>> = callbackFlow {
        val request = fireStore.collection("recommendation")
            .whereEqualTo("user_id", currentUser?.uid)
            .orderBy("relevance_index", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, fireStoreExcept ->
                if (fireStoreExcept != null) {
                    return@addSnapshotListener
                }
                trySend(
                    snapshot!!.documentChanges.map {
                        val docID = it.document.id
                        val hash = it.document.data
                        hash["doc_id"] = docID
                        hash["action_flag"] = it.type
                        //Log.e("REPOSITORY", "${hash["relevance_index"]}")
                        hash
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

    /*
     fun fetchRecommendation(
         collectionName: String="recommendation"
     ):Flow<List<MutableMap<String, Any>>> = callbackFlow {
         val request = fireStore.collection(collectionName)
             .whereEqualTo("user_id", currentUser?.uid)
             .addSnapshotListener { snapshot, fireStoreExcept ->
                 if (fireStoreExcept != null) {
                     Log.e("Firestore Exception in Flow", "${fireStoreExcept.message}")
                     return@addSnapshotListener
                 }
                 trySend(snapshot!!.documentChanges.map { it.document.data })
             }

         awaitClose { request.remove() }
    }.flowOn(Dispatchers.IO)
     */
}