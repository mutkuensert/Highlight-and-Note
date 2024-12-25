package com.mutkuensert.highlightandnote.feature.note.core.compose

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mutkuensert.highlightandnote.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Swipeable(
    snackbarHostState: SnackbarHostState,
    onSnackbarDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true,
    gesturesEnabled: Boolean = true,
    backgroundContent: @Composable() (RowScope.() -> Unit),
    content: @Composable RowScope.() -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isNoteVisible = remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { dismissValue ->
        handleValueChange(
            context,
            dismissValue,
            isNoteVisible,
            coroutineScope,
            snackbarHostState,
            onSnackbarDismissed
        )
    })

    LaunchedEffect(isNoteVisible.value) {
        if (isNoteVisible.value) {
            dismissState.reset()
        }
    }

    AnimatedVisibility(isNoteVisible.value) {
        SwipeToDismissBox(
            dismissState,
            backgroundContent,
            modifier,
            enableDismissFromStartToEnd,
            enableDismissFromEndToStart,
            gesturesEnabled,
            content
        )
    }
}

private fun handleValueChange(
    context: Context,
    dismissValue: SwipeToDismissBoxValue,
    isVisible: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onSnackbarDismissed: () -> Unit,
): Boolean {
    if (dismissValue == SwipeToDismissBoxValue.Settled) {
        return true
    }

    if (!isVisible.value) {
        return false
    }

    coroutineScope.launch {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = context.getString(R.string.do_you_want_to_undo_deletion),
            actionLabel = context.getString(R.string.undo),
            duration = SnackbarDuration.Short
        )

        if (snackbarResult == SnackbarResult.Dismissed) {
            onSnackbarDismissed.invoke()
        } else {
            isVisible.value = true

        }
    }
    isVisible.value = false
    return true
}