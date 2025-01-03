package com.example.personal_feed.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firestore.FirestoreRepository
import com.example.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailVM @Inject constructor(
    val firestoreRepository: FirestoreRepository,
    val movieRepository: MovieRepository
): ViewModel() {

    private val _uiState: MutableLiveData<CardDetailUiState> = MutableLiveData()
    val uiState = _uiState

    suspend fun getDetail(
        downloadResID: String,
        docID: String
    ) {
        firestoreRepository.cashedDoc.collect { docCash ->
            val itemContent = movieRepository.getMovie(downloadResID)
            val itemMeta = docCash[docID]
            Log.e("In CardDetailVm", "$docCash")
            _uiState.value =  CardDetailUiState(
                image = itemContent!!.downloadImage!!,
                title = itemContent.title,
                imdbScore = itemContent.imdbRating,
                ageRating = itemContent.rated,
                duration = itemContent.runtime,
                director = itemContent.director,
                genre = itemContent.genre,
                description = itemContent.plot,
                inPersonalList = itemMeta!!.marked,
                userRating = itemMeta.rated
            )
        }
    }
}

data class CardDetailUiState(
    val image: ImageBitmap,
    val title: String,
    val imdbScore: String,
    val ageRating: String,
    val duration: String,
    val director: String,
    val genre: String,
    val description: String,
    val inPersonalList: Boolean,
    val userRating: Int?
)
