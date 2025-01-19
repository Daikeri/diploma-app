package com.example.authorization.ui.screen

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.authorization.R

@Preview
@Composable
fun StartScreen() {
    val mainSurfaceColor = Color(0xFF141414)
    val titleColor = Color(0xFFFFFFFF)
    val inputFieldColor = Color(0xFF292929)
    val inputFieldContentColor = Color(0xFFC4C4C4)
    val logInButtonColor = Color(0xFF38E078)
    val logInButtonContentColor = Color(0xFF141414)

    val titleContent = "Welcome back"

    val titleFontSettings = FontFamily(Font(resId = R.font.inter_18pt_bold))
    val titleFontSize = 28



    Surface(
        color = mainSurfaceColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1.0f))

            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(
                    content = titleContent,
                    fontSettings = titleFontSettings,
                    fontSize = titleFontSize,
                    color = titleColor
                )
                InputForm()
                LogInButton()
                SignUpButton()
            }
        }
    }
}


@Composable
fun Title(
    content: String,
    fontSettings: FontFamily,
    fontSize: Int,
    color: Color
) {
    Text(
        text = content,
        fontFamily = fontSettings,
        fontSize = fontSize.sp,
        color = color,
    )
}

@Composable
fun InputForm() {

}

@Composable
fun ForgotPasswordButton() {

}

@Composable
fun LogInButton() {

}

@Composable
fun SignUpButton() {

}