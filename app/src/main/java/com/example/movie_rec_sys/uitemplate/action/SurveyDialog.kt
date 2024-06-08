package com.example.movie_rec_sys.uitemplate.action

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movie_rec_sys.data.Movie
import com.example.movie_rec_sys.viewmodel.DialogViewModel

@Composable
fun MyDialog(
    toRecScreen: () -> Unit,
    viewModel: DialogViewModel = viewModel(factory = DialogViewModel.Factory),
) {
    val itemCollection: List<Movie> by viewModel.uiState.observeAsState(emptyList())
    val sendSuccessful: Boolean by viewModel.sendSuccessful.observeAsState(false)
    val dialogNavController = rememberNavController()
    var currentItem by remember { mutableIntStateOf(0) }
    var globalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(itemCollection) {
        if (itemCollection.isEmpty()) {
            viewModel.getItem()
        }
        else {
            globalVisible = true
            currentItem += 1
            dialogNavController.navigate(currentItem.toString())
        }
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Вам интересно следующее?",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .then(if (globalVisible) Modifier.alpha(1f) else Modifier.alpha(0f))
                )

                NavHost(
                    navController = dialogNavController,
                    startDestination = "0",
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .height(250.dp)
                        .width(170.dp)
                        .clip(RoundedCornerShape(28.dp)),
                ) {
                    composable("0") {
                        BackHandler(true) {}
                        DialogDownloadAnimate()
                    }
                    composable("1") {
                        BackHandler(true) {}
                        DialogPoster2(itemCollection[currentItem-1].downloadImage)
                    }
                    composable("2") {
                        BackHandler(true) {
                            dialogNavController.popBackStack()
                            currentItem -= 1
                        }
                        DialogPoster2(itemCollection[currentItem-1].downloadImage)
                    }
                    composable("3") {
                        BackHandler(true) {
                            dialogNavController.popBackStack()
                            currentItem -= 1
                        }
                        DialogPoster2(itemCollection[currentItem-1].downloadImage)
                    }
                    composable("4") {
                        BackHandler(true) {
                            dialogNavController.popBackStack()
                            currentItem -= 1
                        }
                        DialogPoster2(itemCollection[currentItem-1].downloadImage)
                    }
                    composable("5") {
                        BackHandler(true) {
                            dialogNavController.popBackStack()
                            currentItem -= 1
                        }
                        DialogPoster2(itemCollection[currentItem-1].downloadImage)
                    }
                    composable("6") {
                        LaunchedEffect(key1 = Unit) {
                            viewModel.sendFeedback()
                        }
                        BackHandler(true) {}
                        DialogDownloadAnimate()
                    }
                }
                Text(
                    text = if (currentItem <= 6 && itemCollection.isNotEmpty() && (currentItem - 1) >= 0) itemCollection[currentItem-1].title else "",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .height(18.dp)
                        .then(if (globalVisible) Modifier.alpha(1f) else Modifier.alpha(0f))

                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val onClickHandlerOnlyFalse = {
                        viewModel.markItem(currentItem.toString(), false)
                        if (currentItem < 6) {
                            globalVisible = currentItem != 5
                            if(!globalVisible) {
                                dialogNavController.navigate((currentItem+1).toString())
                            }
                            else {
                                currentItem += 1
                                dialogNavController.navigate(currentItem.toString()) {}
                            }

                        }
                    }
                    val onClickHandlerOnlyTrue = {
                        viewModel.markItem(currentItem.toString(), true)
                        if (currentItem < 6) {
                            globalVisible = currentItem != 5
                            if(!globalVisible) {
                                dialogNavController.navigate((currentItem+1).toString())
                            }
                            else {
                                currentItem += 1
                                dialogNavController.navigate(currentItem.toString()) {}
                            }

                        }
                    }

                    DialogButton(globalVisible, onClickHandlerOnlyFalse, Modifier.padding(end = 8.dp), "Неитересно")
                    DialogButton(globalVisible, onClickHandlerOnlyTrue, label = "Интересно")
                }
            }
        }
    }
}

@Composable
fun DialogDownloadAnimate() {
    BackHandler(true) {}
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000, easing = LinearEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000, easing = LinearEasing))
    ) {
        BackHandler(true) {}
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp), // Отступы от краев
            color = Color.Blue // Цвет индикатора загрузки
        )
    }
}

@Composable
fun DialogButton(
    visibleState: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    TextButton(
        modifier = Modifier
            .then(modifier)
            .then(
                if (visibleState) Modifier.alpha(1f) else Modifier.alpha(0f)
            ),
        onClick = { onClick() }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun DialogPoster(resource: Int) {
    Image(
        painter = painterResource(resource),
        contentDescription = null,
    )
}

@Composable
fun DialogPoster2(resource: ImageBitmap?) {
    if (resource != null) {
        Image(
            bitmap = resource,
            contentDescription = null,
        )
    }
}

