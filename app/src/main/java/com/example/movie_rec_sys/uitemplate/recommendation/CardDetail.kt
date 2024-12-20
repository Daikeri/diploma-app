package com.example.movie_rec_sys.uitemplate.recommendation

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_rec_sys.viewmodel.CardDetailUiState
import com.example.movie_rec_sys.viewmodel.RecScreenViewModel
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun CardDetail(
    categoryName: String,
    docID: String,
    viewModel: RecScreenViewModel = viewModel()
) {
    val cardUiState by viewModel.cardUiState.observeAsState(CardDetailUiState.getEmptyInstance())
    val tags by produceState(initialValue = listOf(""), key1 = cardUiState) {
        val items =  mutableListOf(
            "Imdb Rating:${cardUiState.item.imdbRating}",
            "Rated:${cardUiState.item.rated}",
        ).plus(cardUiState.item.genre.split(", "))
        value = items.shuffled()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = cardUiState.item.title) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            },
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Picture(image = cardUiState.item.downloadImage!!)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {
            ButtonSet.content.forEach {
                with(it) {
                    ActiveButton(
                        firestoreCollectionKey = firestoreCollectionKey,
                        actualState = when(firestoreCollectionKey) {
                            "marked" -> cardUiState.marked
                            "viewed" -> cardUiState.viewed
                            else -> false
                        },
                        onClick = { firestoreCollection: Pair<String, Any?> ->
                            val hash = mutableMapOf(
                                "doc_id" to docID,
                                "firestore_collection" to firestoreCollection.first,
                                "value" to firestoreCollection.second
                            )
                            //viewModel.uploadUpdates(update = hash)
                        },
                        label = label,
                        iconId = iconId)
                }
            }
        }

        TagBar(tags, modifier = Modifier.padding(4.dp))

        Surface(
            elevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF7F2FA))
            ) {
                Text(
                    text = cardUiState.item.plot,
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun ActiveButton(
    firestoreCollectionKey: String,
    actualState: Boolean,
    onClick: (Pair<String, Any?>) -> Unit,
    label: String,
    iconId: Int,
    clickedColor: Color=Color.hsv(31f, 0.74f, 1.00f),
) {
    val defaultColor = LocalContentColor.current
    var isActive by remember { mutableStateOf(actualState) }
    val viewedButtonColor by animateColorAsState(
        targetValue = if (isActive) clickedColor else defaultColor,
        label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                isActive = !isActive
                onClick(firestoreCollectionKey to isActive)
            }
        ) {
            val icon = painterResource(id = iconId)
            Icon(
                painter = icon,
                contentDescription = null,
                tint = viewedButtonColor
            )
        }

        Text(text = label, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun Picture(image: ImageBitmap) {
    Image(
        bitmap = image,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
            .blur(50.dp)
    )

    Image(
        bitmap = image,
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight()
            .width(204.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(28.dp))
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TagItem(text: String) {
    Chip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = {},
        leadingIcon = {},
        border = BorderStroke(1.dp, Color(0xFF3B3A3C))
    ) {
        Text(text)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagBar(tags: List<String>, modifier: Modifier) {
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        (tags).forEach {
            TagItem(text = it)
        }
    }
}

fun blurBitmap(context: Context, imageBitmap: ImageBitmap, radius: Float): ImageBitmap {
    // Получаем Bitmap из ImageBitmap и создаем его копию для обработки RenderScript
    val inputBitmap = imageBitmap.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, true)

    // Создаем RenderScript и Allocation
    val renderScript = RenderScript.create(context)
    val input = Allocation.createFromBitmap(renderScript, inputBitmap)
    val output = Allocation.createTyped(renderScript, input.type)

    // Применяем размытие
    val script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    script.setRadius(radius)
    script.setInput(input)
    script.forEach(output)

    // Копируем результаты в Bitmap и освобождаем ресурсы RenderScript
    val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, Bitmap.Config.ARGB_8888)
    output.copyTo(outputBitmap)
    renderScript.destroy()

    // Возвращаем результат как ImageBitmap
    return outputBitmap.asImageBitmap()
}

