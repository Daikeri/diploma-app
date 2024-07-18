package com.example.movie_rec_sys.uitemplate.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_rec_sys.R

@Preview
@Composable
fun UserCollectionScreen() {
    Surface(
        color = Color(0xFFFDF7FF),
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                CategoryCard(label = "Вы хотели посмотреть",
                    imagePath = R.drawable.bookmark_star,
                    Modifier
                        .height(200.dp)
                        .padding(bottom = 10.dp))
            }

            item {
                CategoryCard(
                    label = "Оцененное/Просмотренное",
                    imagePath = R.drawable.color_favorite,
                    Modifier
                        .height(200.dp)
                        .padding(bottom = 10.dp)
                )
            }

            item {
                CategoryCard(
                    label = "Добавить папку",
                    imagePath = R.drawable.append,
                    Modifier
                        .height(200.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun CategoryCard(label: String, imagePath: Int, modifier: Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
        onClick = { /*TODO*/ },
        modifier = Modifier
            .then(modifier)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Image(painter = painterResource(id = imagePath), contentDescription = null)
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1C1B20)
            )
        }
    }
}