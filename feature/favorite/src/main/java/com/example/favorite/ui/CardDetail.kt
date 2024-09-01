package com.example.favorite.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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

@Preview
@Composable
fun CardDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        MoviePoster(Modifier.padding(bottom = 8.dp))
        TitleWithButton()
        ScoreWithRate()
        Description()
        TagBar()
    }
}

@Composable
fun MoviePoster(
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
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 4.dp, start = 4.dp)
                ,
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White)
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
                .height(if (currentLineNum == 1) 36.dp else 72.dp)
                .padding(end = 8.dp)
            ,
            softWrap = true,
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            text = title,
            style = textStyle,
            maxLines = 2,
            overflow = TextOverflow.Clip,
            onTextLayout = { textLayoutResult ->
                currentLineNum = textLayoutResult.lineCount
            }
        )

        Row(

            modifier = Modifier
                .fillMaxWidth()
        ) {
            ElevatedButton(
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = ImageVector.vectorResource(id = R.drawable.bookmark_add_24px),
                    contentDescription = null,
                )
            }

            ElevatedButton(
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = ImageVector.vectorResource(id = R.drawable.star_24px),
                    contentDescription = null
                )
            }
        }

        /*
        IconButton(
            //modifier = Modifier.background(Color.Magenta),
            onClick = { /*TODO*/ },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0x99CDB4DB))

        ) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.bookmark_add_24px), contentDescription = null)
        }

         */
        /*
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                //.background(Color.Magenta)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x99FFC8DD),
                contentColor = Color.Black)
        ) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.star_24px), contentDescription = null)
        }
         */
    }
}

@Composable
fun ScoreWithRate() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE9ECEF), //0xFFE9ECEF 0xFFF8F9FA
            shadowElevation = 0.dp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(50.dp)
                .padding(end = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "IMDb Score: 8.4",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        /*
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8F9FA),
            shadowElevation = 4.dp,
            modifier = Modifier
                .height(50.dp)
                .align(Alignment.CenterEnd)
        ) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "Rated: PG-13",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
         */
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagBar() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8F9FA),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            repeat(5) {
                SuggestionChip(onClick = { /*TODO*/ }, label = { Text(text = "Some tag$it") })
            }
        }
    }
}

@Composable
fun Description() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .size(180.dp)
            .padding(top = 10.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE9ECEF), // 0xFFE9ECEF 0xFFF8F9FA
        shadowElevation = 4.dp
    ) {
       Box {
           Text(
               overflow = TextOverflow.Ellipsis,
               fontFamily = FontFamily(Font(R.font.roboto_regular)) ,
               modifier = Modifier
                   .padding(8.dp)
                   .align(Alignment.Center),
               textAlign = TextAlign.Justify,
               style = MaterialTheme.typography.bodyLarge,
               text = "Tarts? The Queen's argument was, or your Majesty!' the whole party were lying under the tail, And in a great fear of little dog near her, so much right,' said Alice, 'it's very interesting is such VERY long ago anything more nor did not wish I will be civil, you'd take care of them all the dance? \"You have come, so much to her as well as she sentenced were taken the treacle from?' 'You are THESE?' said to get out of the others. 'We called after that lovely garden. I used to your hair goes like." )

       }
    }
}


//0xFFFFC8DD
//0xFFBDE0FE
//0xFFCDB4DB

