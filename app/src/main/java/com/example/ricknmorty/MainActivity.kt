package com.example.ricknmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ricknmorty.models.CharacterViewModel
import com.example.ricknmorty.ui.theme.AppTheme
import com.example.ricknmorty.views.ActionBar
import com.example.ricknmorty.views.CharacterDetailView
import com.example.ricknmorty.views.CharactersListView

object AppDestinations {
    const val CHARACTER_LIST_ROUTE = "characterList"
    const val CHARACTER_DETAIL_ROUTE = "characterDetail"
    const val CHARACTER_ID_ARG = "characterId"
    val CHARACTER_DETAIL_WITH_ARG = "$CHARACTER_DETAIL_ROUTE/{$CHARACTER_ID_ARG}"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                RickAndMortyApp()
            }
        }
    }
}

@Composable
fun RickAndMortyApp(
    navController: NavHostController = rememberNavController(),
    characterViewModel: CharacterViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var currentScreenTitle by rememberSaveable { mutableStateOf("Rick and Morty Characters") }

    Scaffold(
        topBar = {
            val title = when (currentRoute) {
                AppDestinations.CHARACTER_LIST_ROUTE -> "Rick and Morty Characters"
                else -> "Character Details"
            }

            currentScreenTitle = title
            ActionBar(header = currentScreenTitle)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestinations.CHARACTER_LIST_ROUTE,
            modifier = Modifier.padding()
        ) {
            composable(AppDestinations.CHARACTER_LIST_ROUTE) {
                CharactersListView(
                    characterViewModel = characterViewModel,
                    innerPadding = innerPadding,
                    onCharacterClick = { characterId ->
                        navController.navigate(
                            "${AppDestinations.CHARACTER_DETAIL_ROUTE}/$characterId"
                        )
                    }
                )
            }
            composable(
                route = AppDestinations.CHARACTER_DETAIL_WITH_ARG,
                arguments = listOf(navArgument(AppDestinations.CHARACTER_ID_ARG) {
                    type = NavType.IntType
                })
            ) { navBackStackEntry ->
                val characterId = navBackStackEntry
                    .arguments?.getInt(AppDestinations.CHARACTER_ID_ARG)

                if (characterId != null) {
                    val character = characterViewModel.getCharacterById(characterId)

                    if (character != null) {
                        CharacterDetailView(character = character, innerPadding = innerPadding)
                    } else {
                        Text("Error: Character not found.")
                    }
                } else {
                    Text("Error: Character ID not found.")
                }
            }
        }
    }
}
