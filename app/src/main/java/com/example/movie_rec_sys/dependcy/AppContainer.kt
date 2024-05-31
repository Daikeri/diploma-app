package com.example.movie_rec_sys.dependcy

import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.FirebaseAuthRepository
import com.example.movie_rec_sys.data.FirestoreRepository
import com.example.movie_rec_sys.data.ItemRemoteDataSource
import com.example.movie_rec_sys.data.ItemRepository
import com.example.movie_rec_sys.data.PrimaryRecDataSource
import com.example.movie_rec_sys.data.PrimaryRecRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AppContainer(myApplication: MyApplication) {
    private val fireBaseAuth = FirebaseAuth.getInstance()
    val fireBaseRepos = FirebaseAuthRepository(fireBaseAuth)

    private val dockerSource = "http://192.168.31.172:8000"
    private val dockerDS = PrimaryRecDataSource(dockerSource)
    val primaryRecRepos = PrimaryRecRepository(dockerDS)

    private val itemSource = "http://www.omdbapi.com"
    private val apiKey =  "f75f8380"//"67bd6ed"
    private val itemDS = ItemRemoteDataSource(itemSource, apiKey, myApplication)
    val itemRepos = ItemRepository(itemDS)

    private val fireStore = Firebase.firestore
    var fireStoreRepos = FirestoreRepository(fireStore, fireBaseAuth.currentUser)

    init {
        fireBaseRepos.addCallback { fireStoreRepos.setNewUser(it) }
    }
}