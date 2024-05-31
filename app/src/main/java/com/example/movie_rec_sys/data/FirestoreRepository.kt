package com.example.movie_rec_sys.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepository(
    private val fireStore: FirebaseFirestore,
    private var currentUser: FirebaseUser?
) {
    suspend fun setNewUser(newVal: FirebaseUser?) {
        this.currentUser = newVal
    }

    suspend fun fetchRecommendation(
        collectionName: String="recommendation"
    ): List<MutableMap<String, Any>>  {
        return withContext(Dispatchers.IO) {
            val request = fireStore.collection(collectionName)
                .whereEqualTo("user_id", currentUser?.uid)
                .get()
                .await()
            val result = request.map {
                it.data
            }
            result
        }
    }
}