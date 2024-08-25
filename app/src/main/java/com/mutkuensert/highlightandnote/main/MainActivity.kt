package com.mutkuensert.highlightandnote.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.androidsignatureexample.ui.theme.HighlightAndNoteTheme
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.home.HomeRoute
import com.mutkuensert.highlightandnote.home.HomeScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HighlightAndNoteTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = HomeRoute
                    ) {
                        composable<HomeRoute> {
                            HomeScreen({},
                                {
                                    //navigate to new note, with selected text if exists
                                }
                            )
                        }
                    }
                }

                SelectedTextHandler(snackbarHostState, navController)
            }
        }
    }

    @Composable
    private fun SelectedTextHandler(
        snackbarHostState: SnackbarHostState,
        navController: NavController
    ) {
        LaunchedEffect(Unit) {
            val selectedText = getSelectedTextInIntent()

            if (selectedText != null) {
                val result = snackbarHostState
                    .showSnackbar(
                        message = getString(R.string.snack_message),
                        actionLabel = getString(R.string.menu_new),
                        duration = SnackbarDuration.Indefinite
                    )

                if (result == SnackbarResult.ActionPerformed) {
                    navController.navigate(HomeRoute(selectedText)) //To be detail route
                }
            }
        }
    }

    private fun getSelectedTextInIntent(): String? {
        val action = intent.action

        if (action.equals(Intent.ACTION_PROCESS_TEXT) || action.equals(Intent.ACTION_SEND)) {
            return intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                ?: intent.getStringExtra(Intent.EXTRA_TEXT)

        }

        return null
    }
}