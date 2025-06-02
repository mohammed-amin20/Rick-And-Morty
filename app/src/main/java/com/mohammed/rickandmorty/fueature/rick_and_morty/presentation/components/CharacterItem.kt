package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character

@Composable
fun CharacterItem(
    character: Character
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFF3C3E44))
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.45f),
            model = character.image,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(
                text = character.name,
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when (character.status) {
                                "Alive" -> Color.Green
                                "Dead" -> Color.Red
                                else -> Color.Gray
                            }
                        )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${character.status} - ${character.species}",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(4.dp))
            Column {
                Text(
                    text = "Last known location:",
                    fontSize = 12.sp,
                    color = Color(0xff9E9E9E)
                )
//                Spacer(Modifier.height(4.dp))
                Text(
                    text = character.location,
                    fontSize = 16.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(4.dp))
            Column {
                Text(
                    text = "First seen in:",
                    fontSize = 12.sp,
                    color = Color(0xff9E9E9E)
                )
//                Spacer(Modifier.height(4.dp))
                Text(
                    text = character.origin,
                    fontSize = 16.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}