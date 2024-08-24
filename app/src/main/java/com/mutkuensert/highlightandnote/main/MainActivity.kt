package com.mutkuensert.highlightandnote.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.androidsignatureexample.ui.theme.HighlightAndNoteTheme
import com.mutkuensert.highlightandnote.home.HomeRoute
import com.mutkuensert.highlightandnote.home.HomeScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HighlightAndNoteTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = HomeRoute
                ) {
                    composable<HomeRoute>{
                        HomeScreen()
                    }
                }
            }
        }
    }
}