package com.example.ricknmorty.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ricknmorty.models.Character

@Composable
fun CharactersListView(characters: Set<Character>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Can add alphabet letters with h-dividers
        items(
            count = characters.size,
            key = { index -> characters.elementAt(index).id },
            itemContent = { index ->
                CharacterCard(characters.elementAt(index))
            }
        )
    }
}

@Composable
fun CharacterCard(character: Character) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        //AsyncImage() as background - image from URL with Coil
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = Float.POSITIVE_INFINITY / 2f,
                    )
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = character.name,
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontSize = 24.sp
            )
            HorizontalDivider(
                modifier = Modifier.padding(4.dp),
            )
            Text(
                text = character.getEpisodesCount().toString(),
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
