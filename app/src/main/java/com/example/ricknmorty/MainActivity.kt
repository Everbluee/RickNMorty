package com.example.ricknmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ricknmorty.ui.theme.RickNMortyTheme
import com.example.ricknmorty.views.CharactersListView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickNMortyTheme {
                Scaffold(
                    topBar = {
                        // implement dynamic header Characters/Character Details
                        TopAppBar(
                            header = "Rick and Morty Characters"
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CharactersListView(innerPadding)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(header: String) {
    TopAppBar(
        title = { Text(text = header) },
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary),
        navigationIcon = { Icons.AutoMirrored.Filled.ArrowBack },
    )
}