package com.example.authorization.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authorization.R

@Preview
@Composable
fun SelectionScreen(
    onClickLogIn: () -> Unit = {},
    onClickSignUp: () -> Unit = {}
) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Фон
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.side_wave_background),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )

            // Приветственная надпись
            BackgroundTitle(modifier = Modifier.align(Alignment.TopStart))

            AuthSurface(modifier = Modifier.align(Alignment.Center))
        }
    }
}


@Composable
fun BackgroundTitle(
    modifier: Modifier,
) {
    Box(
        modifier = Modifier
            .then(modifier)
    ) {
        Text(
            text = "Hello\nand welcome",
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.displaySmall.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFCDB4DB),
                        Color(0xFFFFC8DD),
                        Color(0xFFFFAFCC),
                        Color(0xFFBDE0FE),
                        Color(0xFFA2D2FF)
                    )
                ),
                fontSize = 86.sp,
                lineHeight = 88.sp
            )
        )
    }
}

@Composable
fun AuthSurface(modifier: Modifier) {
    val paddingX = 60.dp
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(top = 90.dp, start = paddingX, end = paddingX)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.account_circle_40px),
                contentDescription = null,
                tint = Color(0xFF4F5D75), //0xFFA2D2FF
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp)
            )

            Text(
                text = "Please select the\nappropriate option",
                textAlign = TextAlign.Center,
                color = Color(0xFF495057),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 17.sp
                ),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(55.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 125.dp)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthSurfaceButton(
                    modifier =  Modifier.padding(bottom = 16.dp),
                    text = "Log in"
                )

                AuthSurfaceButton(
                    modifier = modifier,
                    text = "Sign Up"
                )
            }
        }
    }
}

@Composable
fun AuthSurfaceButton(
    modifier: Modifier,
    text: String
) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(180.dp)
            .then(modifier)
    ) {
        Text(
            text = "Sign up",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF495057), //0xFFA2D2FF 0xFF343A40
            modifier = Modifier
        )
    }
}

