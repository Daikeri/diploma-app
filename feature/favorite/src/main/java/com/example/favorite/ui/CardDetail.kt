package com.example.favorite.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.favorite.R
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.days
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.MultiParagraph
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize

@Composable
fun CardDetailScreen(widthState: Dp, heightState:Dp, onClickBackButton: () -> Unit = {}) {
    val listState = rememberLazyListState()

    var isDescriptionExpand by remember {
        mutableStateOf(false)
    }

    val onClickAtDescription = { isDescriptionExpand = !isDescriptionExpand }

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


    Surface(color = Color(0xFF141414), modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                //.fillMaxSize()
                .width(widthState)
                .height(heightState)
                .padding(horizontal = 10.dp),
            state = listState
        ) {
            item {
                MoviePoster(
                    onClickBackButton,
                    Modifier.padding(bottom = 8.dp),
                )
            }

            item {
                TitleWithButton()
            }

            item {
                TagBar()
            }

            item(key = "description") {
                Description(isDescriptionExpand,onClickAtDescription)
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

@Composable
fun MoviePoster(
    onClickBackButton: () -> Unit,
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
                onClick = { onClickBackButton() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 4.dp, start = 4.dp)
                ,
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.2f)) // 0xFFFFFFFF 0xFF141414
            ) {
                Icon(
                    modifier = Modifier.alpha(1.0f),
                    tint = Color(0xFFFFFFFF),
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null,
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
                .height(if (currentLineNum == 1) 36.dp else 72.dp)
                .padding(end = 8.dp)
            ,
            softWrap = true,
            fontFamily = FontFamily(Font(R.font.inter_18pt_bold)),
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
fun TagBar() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        TagItem(title = "IMDb Score: 8.4", modifier = Modifier.weight(2.0f))
        TagItem(title = "PG-13",modifier = Modifier.weight(1.0f))
        TagItem(title = "143 min",modifier = Modifier.weight(1.0f))
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        TagItem(title = "Kevin Reynolds",modifier = Modifier.weight(1.0f))
        TagItem(title = "Action, Adventure, Drama", modifier = Modifier.weight(2.0f))
    }
}

@Composable
fun TagItem(title: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF292929) , //0xFFE9ECEF 0xFFF8F9FA свежак - Color(0xFF292929)
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
                modifier = Modifier, //.padding(8.dp)
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
                   .align(Alignment.Center)
           )
       }
    }
}

@Composable
fun Description(isExpand: Boolean, onClickAtDescription: () -> Unit) {
    val description = "Tarts? The Queen's argument was, or your Majesty!' the whole party were lying under the tail," +
            " And in a great fear of little dog near her, so much right,' said Alice," +
            " 'it's very interesting is such VERY long ago anything more nor did not wish I will be civil," +
            " you'd take care of them all the dance? \"You have come, so much to her as well as she sentenced were taken the treacle from?'" +
            " 'You are THESE?' said to get out of the others. 'We called after that lovely garden." +
            " I used to your hair goes like." +
            "Tarts? The Queen's argument was, or your Majesty!' the whole party were lying under the tail," +
            " And in a great fear of little dog near her, so much right,' said Alice," +
            " 'it's very interesting is such VERY long ago anything more nor did not wish I will be civil," +
            " you'd take care of them all the dance? \"You have come, so much to her as well as she sentenced were taken the treacle from?'" +
            " 'You are THESE?' said to get out of the others. 'We called after that lovely garden." +
            " I used to your hair goes like."

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
                    indication = rememberRipple(),
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

@Composable
fun measureTextSize(text: String, minLines:Int): Dp  {
    val textMeasurer = rememberTextMeasurer()
    val result = textMeasurer.measure(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
    )

    Log.e("RESULT FROM MEASURE", "${result.size.height}")
    return with(LocalDensity.current) {
        (minLines * result.size.height).toDp()
    }
}

@Composable
fun measureTextSizeVer2(
    text: String,
    constraintNumLine: Int,
    containerWidth: Int
): Pair<Dp, Dp>  {
    val textMeasurer = rememberTextMeasurer()

    val inCloseState = textMeasurer.measure(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = constraintNumLine,
        constraints = Constraints(
            maxWidth = containerWidth
        )
    )

    val inOpenState = textMeasurer.measure(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        constraints = Constraints(
            maxWidth = containerWidth
        )
    )

    Log.e("MEASURE", "${inCloseState.size}|${inOpenState.size}")
    val result = with(LocalDensity.current) {
        Pair(
            first = inCloseState.size.height.toDp(),
            second = inOpenState.size.height.toDp()
        )
    }
    Log.e("MEASURE IN DP", "$result")
    return result
}


