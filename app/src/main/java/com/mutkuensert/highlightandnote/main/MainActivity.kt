package com.mutkuensert.highlightandnote.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.core.AppNavigator
import com.mutkuensert.highlightandnote.feature.note.presentation.detail.DetailRoute
import com.mutkuensert.highlightandnote.feature.note.presentation.detail.DetailScreen
import com.mutkuensert.highlightandnote.feature.note.presentation.notes.NotesRoute
import com.mutkuensert.highlightandnote.feature.note.presentation.notes.NotesScreen
import com.mutkuensert.highlightandnote.theme.HighlightAndNoteTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appNavigator.setActivity(this)
        enableEdgeToEdge()

        setContent {
            HighlightAndNoteTheme {
                AppContent()
            }
        }
    }

    @Composable
    private fun AppContent() {
        val navController = rememberNavController()

        LaunchedEffect(navController) {
            appNavigator.setNavHostController(navController)
        }

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        SelectedTextHandler(snackbarHostState, navController)
    }

    @Composable
    private fun AppNavHost(
        navController: NavHostController,
        paddingValues: PaddingValues,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            modifier = modifier.padding(paddingValues),
            navController = navController,
            startDestination = NotesRoute()
        ) {
            composable<NotesRoute> {
                NotesScreen(getSelectedTextInIntent())
            }

            composable<DetailRoute> {
                DetailScreen()
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
                val result = snackbarHostState.showSnackbar(
                    message = getString(R.string.snack_message),
                    actionLabel = getString(R.string.menu_new),
                    duration = SnackbarDuration.Long
                )

                if (result == SnackbarResult.ActionPerformed) {
                    navController.navigate(DetailRoute(text = selectedText))
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