package com.example.movie_rec_sys.uitemplate.movie_feed_card

import androidx.compose.ui.graphics.Color
import com.example.movie_rec_sys.R

object ButtonSet {
    val content = listOf(
        ButtonArguments(
            firestoreCollectionKey = "rated",
            label = "Оценить",
            iconId =  R.drawable.grade
        ),
        ButtonArguments(
            firestoreCollectionKey = "marked",
            label = "Мое",
            iconId = R.drawable.bookmark
        ),
        ButtonArguments(
            firestoreCollectionKey = "viewed",
            label = "Просмотрено",
            iconId = R.drawable.visibility
        )
    )
}

data class ButtonArguments(
    val firestoreCollectionKey: String,
    val label: String,
    val iconId: Int,
    val clickedColor: Color = Color.hsv(31f, 0.74f, 1.00f),
)

fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

fun Int.toBoolean(): Boolean {
    return this != 0
}


/*
@Composable
fun ButtonPanel() {
    val clickedColor = Color.hsv(31f, 0.74f, 1.00f)
    val defaultColor = LocalContentColor.current
    var isClickedViewed by remember { mutableStateOf(false) }
    var isClickedMarked by remember { mutableStateOf(false) }

    val viewedButtonColor by animateColorAsState(
        targetValue = if (isClickedViewed) clickedColor else defaultColor,
        label = ""
    )
    val markedButtonColor by animateColorAsState(
        targetValue = if (isClickedMarked) clickedColor else defaultColor,
        label = ""
    )

    Row(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = {

                }
            ) {
                val icon = painterResource(id = R.drawable.grade)
                Icon(
                    painter = icon,
                    tint = LocalContentColor.current,
                    contentDescription = null
                )
            }
            Text(text = "Оценить", fontSize = 12.sp, textAlign = TextAlign.Center)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = {
                    isClickedMarked = !isClickedMarked
                }
            ) {
                val icon = painterResource(id = R.drawable.bookmark)
                Icon(
                    painter = icon,
                    tint = markedButtonColor,
                    contentDescription = null
                )
            }
            Text(text = "Мое", fontSize = 12.sp, textAlign = TextAlign.Center)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = {
                    isClickedViewed = !isClickedViewed
                }
            ) {
                val icon = painterResource(id = R.drawable.visibility)
                Icon(
                    painter = icon,
                    tint = viewedButtonColor,
                    contentDescription = null
                )
            }
            Text(text = "Видел", fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}
 */