package com.example.ricknmorty.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ricknmorty.models.Character
import com.example.ricknmorty.models.CharacterViewModel

@Composable
fun CharactersListView(
    characterViewModel: CharacterViewModel,
    innerPadding: PaddingValues,
    onCharacterClick: (Int) -> Unit
) {
    val characterSet by characterViewModel.characterList.observeAsState(initial = emptySet())
    val isLoading by characterViewModel.loading.observeAsState(initial = false)
    val error by characterViewModel.characterListError.observeAsState(false)

    val characterSetSorted = characterSet.sortedBy { it.name }

    when {
        isLoading -> {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { CircularProgressIndicator() }
        }

        error -> ErrorDisplay(innerPadding, characterViewModel)
        else -> CharacterList(innerPadding, characterSetSorted, onCharacterClick)
    }
}

@Composable
private fun CharacterList(
    innerPadding: PaddingValues,
    characterSetSorted: List<Character>,
    onCharacterClick: (Int) -> Unit
) {
    val charactersByLetter = characterSetSorted.groupBy { it.name.first().uppercaseChar() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(4.dp),
    ) {
        charactersByLetter.forEach { (letter, charactersInSection) ->
            item {
                Text(
                    text = letter.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(count = (charactersInSection.size + 1) / 2) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    val index1 = rowIndex * 2
                    val index2 = index1 + 1

                    val character1 = charactersInSection.elementAt(index1)
                    CharacterCard(
                        character1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) { onCharacterClick(character1.id) }

                    if (index2 < charactersInSection.size) {
                        val character2 = charactersInSection.elementAt(index2)
                        CharacterCard(
                            character2,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) { onCharacterClick(character2.id) }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorDisplay(
    innerPadding: PaddingValues,
    characterViewModel: CharacterViewModel
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error Icon",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Oops, something went wrong...",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { characterViewModel.fetchData() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Try again!")
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier,
    onClick: (Int) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
            .aspectRatio(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = { onClick(character.id) }
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.BottomStart),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = character.name,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "Number of episodes: ${character.episode.size}",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

