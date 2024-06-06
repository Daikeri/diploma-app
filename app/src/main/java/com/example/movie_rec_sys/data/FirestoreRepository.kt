package com.example.movie_rec_sys.data

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepository(
    private val fireStore: FirebaseFirestore,
    private var currentUser: FirebaseUser?
) {
    suspend fun setNewUser(newVal: FirebaseUser?) {
        this.currentUser = newVal
    }

     fun fetchRecommendation(
         collectionName: String="recommendation"
     ):Flow<List<MutableMap<String, Any>>> = callbackFlow {
         val request = fireStore.collection(collectionName)
             .whereEqualTo("user_id", currentUser?.uid)
             .addSnapshotListener { snapshot, fireStoreExcept ->
                 if (fireStoreExcept != null) {
                     Log.e("Firestore Exception", "${fireStoreExcept.message}")
                     return@addSnapshotListener
                 }
                 trySend(snapshot!!.documentChanges.map { it.document.data })
             }

         awaitClose { request.remove() }
    }
}