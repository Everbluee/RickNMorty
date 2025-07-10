package com.example.ricknmorty.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ricknmorty.models.Character
import com.example.ricknmorty.models.CharacterViewModel


@Composable
fun CharacterDetailView(
    characterId: Int,
    characterViewModel: CharacterViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val character = characterViewModel.selectedCharacter.observeAsState(initial = null)
    val isLoading = characterViewModel.loadingCharacter.observeAsState(initial = false)
    val error by characterViewModel.characterDetailError.observeAsState(false)

    LaunchedEffect(characterId) {
        characterViewModel.getCharacterById(characterId)
    }

    LaunchedEffect(error) {
        if (error) {
            snackbarHostState.showSnackbar("Oops, something went wrong...")
            navController.popBackStack()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    character.value?.let {
                        CharacterImage(it)
                        CharacterInfo(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterInfo(character: Character) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DetailRow("Status:", character.status)
            DetailRow("Species:", character.species)

            if (character.type.isNotEmpty()) {
                DetailRow("Type:", character.type)
            }

            DetailRow("Gender:", character.gender)
            DetailRow("Origin:", character.origin.name)
            DetailRow("Created:", character.whenCreated())

            if (character.episode.isNotEmpty()) {
                EpisodesList("Episodes:", character.episode)
            } else {
                DetailRow("Episodes:", "N/A")
            }
        }
    }
}

@Composable
fun DetailText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.End
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        DetailText(text = value)
    }
}

@Composable
fun EpisodesList(label: String, episodes: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Column(horizontalAlignment = Alignment.End) {
            episodes.take(5).forEach { episodeUrl ->
                val episodeNumber = episodeUrl.substringAfterLast("/")
                DetailText(text = "Episode $episodeNumber")
            }

            if (episodes.size > 5) {
                DetailText(text = "...and ${episodes.size - 5} more")
            }
        }
    }
}


@Composable
fun CharacterImage(character: Character) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier.aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface),
                    )
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            CharacterName(character)
        }
    }
}

@Composable
private fun CharacterName(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.displayMedium,
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    }
}