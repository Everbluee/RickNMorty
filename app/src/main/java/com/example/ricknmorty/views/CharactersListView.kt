package com.example.ricknmorty.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ricknmorty.models.Character
import com.example.ricknmorty.models.CharacterViewModel

@Composable
fun CharactersListView(innerPadding: PaddingValues) {
    val characterViewModel = CharacterViewModel()
    val characterSet by characterViewModel.data.observeAsState(initial = emptySet())
    val isLoading by characterViewModel.loading.observeAsState(initial = false)
    var selected by remember { mutableStateOf<Character?>(null) }

    val characterSetSorted = characterSet.sortedBy { it.name }

    if (isLoading) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(50.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(4.dp),
        ) {
            // Can add alphabet letters with h-dividers
            items(
                count = characterSetSorted.size,
                key = { index -> characterSetSorted.elementAt(index).id },
                itemContent = { index ->
                    CharacterCard(characterSetSorted.elementAt(index))
                }
            )
        }
    }
}

@Composable
fun CharacterCard(character: Character) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
    ) {
        //AsyncImage() as background - image from URL with Coil
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = Float.POSITIVE_INFINITY / 2f,
                        endY = Float.POSITIVE_INFINITY
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
