package com.example.movie_rec_sys.uitemplate.action

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.viewmodel.CardDetailUiState
import com.example.movie_rec_sys.viewmodel.DialogUiState
import com.example.movie_rec_sys.viewmodel.RecScreenUiState
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel
import com.example.movie_rec_sys.viewmodel.UpdatedDialogViewModel

@Composable
fun UpdatedSurveyDialog(
    viewModel: UpdatedDialogViewModel = viewModel(factory = UpdatedDialogViewModel.Factory),
    externalViewModel: RecScreenViewModel = viewModel(factory = RecScreenViewModel.Factory),
    onRecDownload: () -> Unit
) {
    val uiState: DialogUiState by viewModel.uiState.observeAsState(DialogUiState.getEmptyInstance())
    val density = LocalDensity.current
    val startDownload: Boolean by viewModel.recommendationDownload.observeAsState(false)
    val downloadDone: RecScreenUiState by externalViewModel.generalUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getItems()
    }

    LaunchedEffect(startDownload) {
        if (startDownload)
            externalViewModel.fetchRecommendation()
    }

    LaunchedEffect(downloadDone) {
        downloadDone?.let {
            onRecDownload()
        }
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler {
            viewModel.itemFromCollect(false)
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(400.dp)
                .width(320.dp)
                .onGloballyPositioned {
                    Log.e(
                        "size",
                        "${with(density) { it.size.height.toDp() }}|${with(density) { it.size.width.toDp() }}"
                    )
                }
        ) {
            if (!uiState.loading) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Вам интересно следующее?",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .height(280.dp)
                            .width(170.dp)
                    ) {
                        Crossfade(
                            targetState = uiState,
                            label = "",
                            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                DialogPoster(
                                    resource = it.item!!.downloadImage,
                                )
                                Text(
                                    text = it.item.title,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }

                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.markItem(false)
                                viewModel.itemFromCollect(true)
                            },
                        ) {
                            Text(
                                text = "Неинтересно",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        TextButton(
                            onClick = {
                                viewModel.markItem(true)
                                viewModel.itemFromCollect(true)
                            }
                        ) {
                            Text(
                                text = "Интересно",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp), // Отступы от краев
                        color = MaterialTheme.colorScheme.primary // Цвет индикатора загрузки
                    )
                }
            }
        }
    }
}

@Composable
fun DialogPoster(resource: ImageBitmap?) {
    if (resource != null) {
        Image(
            bitmap = resource,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
    }
}