package com.example.movie_rec_sys.dependcy

import androidx.room.Room
import com.example.movie_rec_sys.MyApplication
import com.example.movie_rec_sys.data.AppDB
import com.example.movie_rec_sys.data.DBRepository

import com.example.movie_rec_sys.data.FirebaseAuthRepository
import com.example.movie_rec_sys.data.FirestoreRemoteSource
import com.example.movie_rec_sys.data.ItemRemoteDataSource
import com.example.movie_rec_sys.data.FSRepository
import com.example.movie_rec_sys.data.PrimaryRecDataSource
import com.example.movie_rec_sys.data.PrimaryRecRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AppContainer(myApplication: MyApplication) {
    private val fireBaseAuth = FirebaseAuth.getInstance()
    val fireBaseRepos = FirebaseAuthRepository(fireBaseAuth)

    private val dockerSource = "http://192.168.31.172:8000"
    private val dockerDS = PrimaryRecDataSource(dockerSource)
    val primaryRecRepos = PrimaryRecRepository(dockerDS)

    private val itemSource = "http://www.omdbapi.com"
    private val apiKey =  "67bd6ed"//"67bd6ed" f75f8380
    private val itemRDS = ItemRemoteDataSource(itemSource, apiKey, myApplication)


    private val fireStore = Firebase.firestore
    var fireStoreRepos = FirestoreRemoteSource(fireStore, fireBaseAuth.currentUser)
    val itemRepos = FSRepository(itemRDS, fireStoreRepos)

    private val db = Room.databaseBuilder(
        context = myApplication,
        AppDB::class.java,
        "database"
    )
        .createFromAsset("movies.db")
        .fallbackToDestructiveMigration()
        .build()

    val dbRepos = DBRepository(
        db,
        itemRDS
    )

    init {
        fireBaseRepos.addCallback { fireStoreRepos.setNewUser(it) }
        fireStore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }

    }
}