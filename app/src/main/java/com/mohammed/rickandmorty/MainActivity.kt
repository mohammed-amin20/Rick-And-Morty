package com.mohammed.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import com.mohammed.rickandmorty.fueature.rick_and_morty.presentation.navigation.RickAndMortyNavHost
import com.mohammed.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                SharedTransitionLayout {
                    RickAndMortyNavHost(sharedTransitionScope = this)
                }
            }
        }
    }
}
