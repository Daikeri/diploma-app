package com.example.movie_rec_sys.uitemplate.authorization

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.R
import com.example.movie_rec_sys.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun GenderScreen(
    viewModel: AuthViewModel,
    toNextScreen: () -> Unit
)  {
    Log.e("viewModel", "${viewModel.hashCode()}")
    var choose by remember { mutableStateOf(false) }

    var dynamicHeight = if (choose) 260.dp else 190.dp

    val animateHeight by animateDpAsState(targetValue = dynamicHeight, label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .height(animateHeight)
                .width(310.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor =  MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 0.dp, top = 0.dp)
                        .align(Alignment.TopCenter)

                ) {
                    Text(
                        text = "Ваш пол",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier

                    ) {
                        CardWithIcon(
                            image = R.drawable.male,
                            label = "Мужской",
                            false, 125,
                            110,
                            onChooseChange = { choose = !choose },
                            addKey = { viewModel.addMetaData("gender", "M") }
                        )
                        CardWithIcon(
                            image = R.drawable.female,
                            label = "Женский",
                            false, 125,
                            110,
                            onChooseChange = { choose = !choose },
                            addKey = { viewModel.addMetaData("gender", "F") }
                        )
                    }
                }

                Crossfade(
                    targetState = choose,
                    animationSpec = tween(easing = FastOutSlowInEasing, durationMillis = 20, delayMillis = 5),
                    label = "",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    if (it) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {
                            Button(
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    bottomStart = 12.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 12.dp
                                ),
                                onClick = { toNextScreen() },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Image(painter = painterResource(id = R.drawable.arrow_forward_base), contentDescription = null )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 120 а 85 для карточки возрастов
@Composable
fun AgeGroupScreen(
    viewModel: AuthViewModel,
    toNextScreen: () -> Unit
) {
    Log.e("viewModel", "${viewModel.hashCode()}")
    var choose by remember { mutableStateOf(false) }

    var dynamicHeight = if (choose) 260.dp else 190.dp

    val animateHeight by animateDpAsState(targetValue = dynamicHeight, label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .height(animateHeight)
                .width(310.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor =  MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
           Box(modifier = Modifier.fillMaxSize()) {
               Column(
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally,
                   modifier = Modifier
                       .padding(start = 16.dp, end = 16.dp, bottom = 0.dp, top = 0.dp)
                       .align(Alignment.TopCenter)

               ) {
                   Text(
                       text = "Ваша возрастная группа",
                       style = MaterialTheme.typography.titleMedium,
                       color = MaterialTheme.colorScheme.onPrimaryContainer,
                       textAlign = TextAlign.Center,
                       modifier = Modifier
                           .padding(top = 10.dp, bottom = 16.dp)
                   )
                   Row(
                       horizontalArrangement = Arrangement.spacedBy(8.dp),
                       modifier = Modifier

                   ) {
                       CardWithIcon(
                           image = R.drawable.boy,
                           label = "до 25",
                           false,
                           120,
                           85,
                           onChooseChange = { choose = !choose },
                           addKey = { viewModel.addMetaData("age", "up_to_25") }
                       )
                       CardWithIcon(
                           image = R.drawable.man,
                           label = "25-49",
                           false,
                           120,
                           85,
                           onChooseChange = { choose = !choose },
                           addKey = { viewModel.addMetaData("age", "25_49") }
                       )
                       CardWithIcon(
                           image = R.drawable.elderly,
                           label = "50+",
                           false,
                           120,
                           85,
                           onChooseChange = { choose = !choose },
                           addKey = { viewModel.addMetaData("age", "50_or_more") }
                       )
                   }
               }

              Crossfade(
                  targetState = choose,
                  animationSpec = tween(easing = FastOutSlowInEasing, durationMillis = 20, delayMillis = 5),
                  label = "",
                  modifier = Modifier
                      .align(Alignment.BottomCenter)
              ) {
                  if (it) {
                      Row(
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.Center,
                          modifier = Modifier
                              .align(Alignment.BottomCenter)
                      ) {
                          Button(
                              shape = RoundedCornerShape(
                                  topStart = 0.dp,
                                  bottomStart = 12.dp,
                                  topEnd = 0.dp,
                                  bottomEnd = 12.dp
                              ),
                              onClick = {
                                  viewModel.uploadMetaData()
                                  toNextScreen()
                              },
                              modifier = Modifier
                                  .fillMaxWidth()
                          ) {
                              Image(painter = painterResource(id = R.drawable.arrow_forward_base), contentDescription = null )
                          }
                      }
                  }
              }
           }
        }
    }
}

@Composable
fun CardWithIcon(
    image: Int,
    label: String,
    initialState: Boolean,
    height: Int,
    width: Int,
    onChooseChange: () -> Unit,
    addKey: () -> Unit
) {
    var enabled by remember { mutableStateOf(initialState) }

    Card(
        onClick = {
            enabled = !enabled
            onChooseChange()
            addKey()
        },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor =  MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ) ,
        modifier = Modifier
            .height(height.dp)
            .width(width.dp)
            .then(if (enabled) Modifier.alpha(1.0f) else Modifier.alpha(0.38f))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Image(painter = painterResource(id = image), contentDescription = null)
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}