package com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.detail.CharacterDetailScreen
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.list.CharacterListScreen

private const val TRANSITION_DURATION = 420

@Composable
fun RickAndMortyNavHost(
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CharactersRoute,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it / 5 },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeIn(tween(TRANSITION_DURATION))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 8 },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeOut(tween(TRANSITION_DURATION))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 8 },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeIn(tween(TRANSITION_DURATION))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it / 5 },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeOut(tween(TRANSITION_DURATION))
        }
    ) {
        composable<CharactersRoute> {
            CharacterListScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                onCharacterClick = { character ->
                    navController.navigate(
                        CharacterDetailRoute(
                            id = character.id,
                            name = character.name,
                            image = character.image
                        )
                    )
                }
            )
        }
        composable<CharacterDetailRoute> {
            CharacterDetailScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
