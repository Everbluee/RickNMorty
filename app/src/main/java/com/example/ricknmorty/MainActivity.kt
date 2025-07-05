package com.example.ricknmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ricknmorty.models.CharacterViewModel
import com.example.ricknmorty.ui.theme.AppTheme
import com.example.ricknmorty.views.ActionBar
import com.example.ricknmorty.views.CharactersListView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val characterViewModel = CharacterViewModel()

        setContent {
            AppTheme {
                Scaffold(
                    topBar = {
                        // implement dynamic header Characters/Character Details
                        ActionBar(header = "Rick and Morty Characters")
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CharactersListView(characterViewModel, innerPadding)
                }
            }
        }
    }
}