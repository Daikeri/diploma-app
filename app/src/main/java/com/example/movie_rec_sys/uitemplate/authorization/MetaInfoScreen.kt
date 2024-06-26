package com.example.movie_rec_sys.uitemplate.authorization

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.movie_rec_sys.R

@Composable
fun GenderScreen() {
    var choose by remember { mutableStateOf(false) }

    var dynamicHeight = if (choose) 250.dp else 200.dp

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
               .width(330.dp),
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
           Box(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 8.dp)
           ) {
               Text(
                   text = "Выберите ваш пол",
                   style = MaterialTheme.typography.titleLarge,
                   color = MaterialTheme.colorScheme.onPrimaryContainer,
                   textAlign = TextAlign.Center,
                   modifier = Modifier
                       .padding(top = 6.dp)
                       .align(Alignment.TopCenter)
               )
               Row(
                   horizontalArrangement = Arrangement.spacedBy(8.dp),
                   modifier = Modifier
                       .align(Alignment.BottomCenter)
               ) {
                   CardWithIcon(image = R.drawable.male, label = "Мужской", false) {
                       choose = !choose
                   }
                   CardWithIcon(image = R.drawable.female, label = "Женский", false ) {
                       choose = !choose
                   }
               }
           }
           if (choose) {
               Row(
                   horizontalArrangement = Arrangement.Center,
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier
               ) {
                   IconButton(onClick = { /*TODO*/ }) {
                       Icons.AutoMirrored.Default.KeyboardArrowRight
                   }
               }
           }
       }
    }
}

// 120 а 85 для карточки возрастов
@Composable
fun AgeGroupScreen() {
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
                       style = MaterialTheme.typography.titleLarge,
                       color = MaterialTheme.colorScheme.onPrimaryContainer,
                       textAlign = TextAlign.Center,
                       modifier = Modifier
                           .padding(top = 10.dp, bottom = 16.dp)
                   )
                   Row(
                       horizontalArrangement = Arrangement.spacedBy(8.dp),
                       modifier = Modifier

                   ) {
                       CardWithIcon(image = R.drawable.boy, label = "до 25", false) {
                           choose = !choose
                       }
                       CardWithIcon(image = R.drawable.man, label = "25-49", false ) {
                           choose = !choose
                       }
                       CardWithIcon(image = R.drawable.elderly, label = "50+", false) {
                           choose = !choose
                       }
                   }
               }
               if (choose) {
                   Log.e("Choose", "${choose}")
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center,
                       modifier = Modifier
                           .align(Alignment.BottomCenter)
                   ) {
                       IconButton(onClick = { /*TODO*/ }) {
                           Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Информация о приложении")
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
    onChooseChange: () -> Unit
) {
    var enabled by remember { mutableStateOf(initialState) }

    Card(
        onClick = {
            enabled = !enabled
            onChooseChange()
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
            .height(120.dp)
            .width(85.dp)
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