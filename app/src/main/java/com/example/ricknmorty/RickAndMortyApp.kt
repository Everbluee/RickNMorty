package com.example.ricknmorty

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.ricknmorty.views.ActionBar
import com.example.ricknmorty.views.CharacterDetailView
import com.example.ricknmorty.views.CharactersListView

@Composable
fun RickAndMortyApp(
    navController: NavHostController = rememberNavController(),
    characterViewModel: CharacterViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val characterIdArg = backStackEntry?.arguments?.getInt(AppRoutes.CHARACTER_ID_ARG)

    var currentScreenTitle by rememberSaveable { mutableStateOf("Rick and Morty Characters") }

    val selectedCharacter by characterViewModel.selectedCharacter.observeAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(characterIdArg) {
        if (characterIdArg != null) {
            characterViewModel.getCharacterById(characterIdArg)
        }
    }

    LaunchedEffect(currentRoute, selectedCharacter) {
        currentScreenTitle = when {
            currentRoute == AppRoutes.CHARACTER_LIST_ROUTE -> "Rick and Morty Characters"
            currentRoute?.startsWith(AppRoutes.CHARACTER_DETAIL_ROUTE) == true -> {
                selectedCharacter?.name ?: "Rick and Morty Characters"
            }

            else -> "Rick and Morty Characters"
        }
    }

    Scaffold(
        topBar = { ActionBar(header = currentScreenTitle) },
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.CHARACTER_LIST_ROUTE,
            modifier = Modifier.padding()
        ) {
            composable(AppRoutes.CHARACTER_LIST_ROUTE) {
                CharactersListView(
                    characterViewModel = characterViewModel,
                    innerPadding = innerPadding,
                    onCharacterClick = { characterId ->
                        navController.navigate(
                            "${AppRoutes.CHARACTER_DETAIL_ROUTE}/$characterId"
                        )
                    }
                )
            }
            composable(
                route = AppRoutes.CHARACTER_DETAIL_WITH_ARG,
                arguments = listOf(navArgument(AppRoutes.CHARACTER_ID_ARG) {
                    type = NavType.IntType
                })
            ) { navBackStackEntry ->
                val characterId = navBackStackEntry
                    .arguments?.getInt(AppRoutes.CHARACTER_ID_ARG)

                if (characterId != null) {
                    CharacterDetailView(
                        characterId = characterId,
                        characterViewModel = characterViewModel,
                        innerPadding = innerPadding,
                        navController = navController,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}
