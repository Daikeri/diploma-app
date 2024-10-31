package com.example.favorite

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.favorite.ui.CardDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = Color.White.toArgb()
                }
            }
            Surface(
                Modifier.fillMaxSize(),
                color = Color.White
            ) {
                Temp3()
            }
        }
    }
}





@Composable
fun Temp3() {
    Box(modifier = Modifier.fillMaxSize()) {
        var openCardDetail by remember {
            mutableStateOf(false)
        }

        val dynamicWidth by animateDpAsState(targetValue = if (openCardDetail) 450.dp else 0.dp, label = "",
            animationSpec = tween(
                durationMillis = 1000,
                easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
            ))

        val dynamicHeight by animateDpAsState(targetValue = if (openCardDetail) 850.dp else 0.dp, label = "",
            animationSpec = tween(
                durationMillis = 800,
                easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
            ))

        Surface(
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Box(modifier = Modifier
                .size(width = 240.dp, height = 120.dp)
                .clickable { openCardDetail = !openCardDetail }
                .zIndex(2f)
            ) {

            }
        }
        CardDetailScreen(
            widthState = dynamicWidth,
            heightState = dynamicHeight,
            onClickBackButton = {openCardDetail = ! openCardDetail}
        )
    }
}