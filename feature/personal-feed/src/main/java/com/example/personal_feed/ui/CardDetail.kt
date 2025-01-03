package com.example.personal_feed.ui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.personal_feed.R
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.personal_feed.viewmodel.CardDetailVM

@Composable
fun CardDetail(
    downloadResID: String,
    docID: String,
    viewModel: CardDetailVM = hiltViewModel(),
    onBackButtonClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.observeAsState()

    val listState = rememberLazyListState()

    var isDescriptionExpand by remember {
        mutableStateOf(false)
    }

    val onClickAtDescription = { isDescriptionExpand = !isDescriptionExpand }

    LaunchedEffect(key1 = Unit ) {
        viewModel.getDetail(downloadResID, docID)
    }

    LaunchedEffect(key1 = isDescriptionExpand) {
        if (isDescriptionExpand) {
            val targetItem = listState.layoutInfo.visibleItemsInfo[3]
            if (targetItem.key == "description") {
                snapshotFlow { listState.layoutInfo.viewportEndOffset }
                    .collect {
                        listState.animateScrollBy(
                            value = targetItem.offset.toFloat(),
                            animationSpec = tween(
                                durationMillis = 1000,
                                easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
                            )
                        )
                    }
            }
        }
    }

    uiState?.let {
        Surface(
            color = Color(0xFF141414),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                state = listState
            ) {
                item {
                    MoviePoster(
                        onBackButtonClick,
                        Modifier.padding(bottom = 8.dp),
                        image = uiState!!.image
                    )
                }

                item {
                    TitleWithButton(title = uiState!!.title)
                }

                item {
                    TagBar(
                        imdbScore = uiState!!.imdbScore,
                        ageRating = uiState!!.ageRating,
                        duration = uiState!!.duration,
                        director = uiState!!.director,
                        genre = uiState!!.genre
                    )
                }

                item(key = "description") {
                    Description(
                        uiState!!.description,
                        isDescriptionExpand,
                        onClickAtDescription
                    )
                }

                item {
                    Stub(R.drawable.peak_background)
                }

                item {
                    Stub(imageId = R.drawable.blurry_background, modifier = Modifier.blur(50.dp))
                }
            }
        }

    }
}

@Composable
fun MoviePoster(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    image: ImageBitmap? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .then(modifier)
            .clip(RoundedCornerShape(12.dp))
    ) {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(50.dp)
            )

            IconButton(
                onClick = { onBackButtonClick() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 4.dp, start = 4.dp)
                ,
                colors = IconButtonDefaults.iconButtonColors(containerColor =  Color(0xFFFFFFFF).copy(alpha = 0.2f))
            ) {
                Icon(
                    tint = Color(0xFFFFFFFF),
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null
                )
            }

            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(204.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(28.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.test_image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(50.dp)

            )

            IconButton(
                onClick = { onBackButtonClick() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 4.dp, start = 4.dp)
                ,
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null
                )
            }

            Image(
                contentScale = ContentScale.FillBounds,
                painter = painterResource(R.drawable.test_image),
                contentDescription = null,
                modifier = Modifier
                    .height(260.dp)
                    .width(180.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}


@Composable
fun TitleWithButton(
    modifier: Modifier = Modifier,
    title:String = "Some title",
) {
    var currentLineNum by remember {
        mutableStateOf<Int?>(null)
    }

    val textStyle =
        if (title.length <= 12)
            MaterialTheme.typography.displaySmall
        else
            MaterialTheme.typography.headlineMedium

    Row(
        modifier = Modifier
    ) {
        Text(
            modifier = Modifier
                .width(225.dp)
                .height(if (currentLineNum == 1) 43.dp else 72.dp)
                .padding(end = 8.dp)
            ,
            softWrap = true,
            fontFamily = FontFamily(Font(R.font.inter_24pt_bold)),
            text = title,
            style = textStyle,
            maxLines = 2,
            overflow = TextOverflow.Clip,
            onTextLayout = { textLayoutResult ->
                currentLineNum = textLayoutResult.lineCount
            },
            color = Color(0xFFFFFFFF)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ElevatedButton(
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38E078), contentColor = Color.Unspecified),
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Icon(
                    tint = Color(0xFF141414),
                    imageVector = ImageVector.vectorResource(id = R.drawable.bookmark_add_colorful),
                    contentDescription = null,
                )
            }

            ElevatedButton(
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38E078)),//0xFFF8F9FA
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    tint = Color(0xFF141414), //0xFFCDB4DB
                    imageVector = ImageVector.vectorResource(id = R.drawable.star_24px),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun TagBar(
    imdbScore: String,
    ageRating: String,
    duration: String,
    director: String,
    genre: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        TagItem(title = "IMDb Score: $imdbScore", modifier = Modifier.weight(2.0f))
        TagItem(title = ageRating, modifier = Modifier.weight(1.0f))
        TagItem(title = duration, modifier = Modifier.weight(1.0f))
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        TagItem(title = director, modifier = Modifier.weight(1.0f))
        TagItem(title = genre, modifier = Modifier.weight(2.0f))
    }
}

@Composable
fun TagItem(title: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF292929), //0xFFE9ECEF 0xFFF8F9FA
        shadowElevation = 0.dp,
        modifier = Modifier
            .height(50.dp)
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFC4C4C4),
                text = title,
                modifier = Modifier,//.padding(8.dp)
                fontFamily = FontFamily(Font(R.font.inter_18pt_regular))
            )
        }
    }
}

@Composable
fun Stub(
    imageId: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                imageVector = ImageVector.vectorResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier)
            )
            Text(
                text = "And some more content...",
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(0.9f)
                    .align(Alignment.Center),
                fontFamily = FontFamily(Font(R.font.inter_18pt_regular))
            )
        }
    }
}

@Composable
fun Description(
    description: String,
    isExpand: Boolean,
    onClickAtDescription: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF292929), // 0xFFE9ECEF 0xFFF8F9FA
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = {
                        onClickAtDescription()
                    }
                )
                .animateContentSize(
                    animationSpec = tween(
                        1000,
                        easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
                    )
                )
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Start,
                maxLines = if (isExpand) Int.MAX_VALUE else 5,
                style = MaterialTheme.typography.bodyLarge,
                text = description,
                color = Color(0xFFFFFFFF),
                fontFamily = FontFamily(Font(R.font.inter_18pt_regular))
            )
        }
    }
}


//0xFFFFC8DD
//0xFFBDE0FE
//0xFFCDB4DB


