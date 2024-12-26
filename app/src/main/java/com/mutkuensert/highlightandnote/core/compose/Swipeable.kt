package com.mutkuensert.highlightandnote.core.compose

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Swipeable(
    snackbarConfig: SnackbarConfig,
    modifier: Modifier = Modifier,
    swipeableConfig: SwipeableConfig = SwipeableConfig(),
    backgroundContent: @Composable RowScope.() -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val isVisible = remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { dismissValue ->
        dismissValue.handleSwipeAction(isVisible, coroutineScope, snackbarConfig)
    })

    LaunchedEffect(isVisible.value) {
        if (isVisible.value) {
            dismissState.reset()
        }
    }

    AnimatedVisibility(isVisible.value) {
        SwipeToDismissBox(
            dismissState,
            backgroundContent,
            modifier,
            swipeableConfig.enableDismissFromStartToEnd,
            swipeableConfig.enableDismissFromEndToStart,
            swipeableConfig.gesturesEnabled,
            content
        )
    }
}

private fun SwipeToDismissBoxValue.handleSwipeAction(
    isVisible: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    snackbarConfig: SnackbarConfig,
): Boolean {
    if (this == SwipeToDismissBoxValue.Settled) {
        return true
    }

    if (!isVisible.value) {
        return false
    }

    coroutineScope.launch {
        val snackbarResult = snackbarConfig.snackbarHostState.showSnackbar(
            message = snackbarConfig.snackbarMessage,
            actionLabel = snackbarConfig.actionLabel,
            duration = snackbarConfig.duration
        )

        if (snackbarResult == SnackbarResult.Dismissed) {
            snackbarConfig.onSnackbarDismissed.invoke()
        } else {
            isVisible.value = true

        }
    }
    isVisible.value = false
    return true
}

class SnackbarConfig(
    val snackbarHostState: SnackbarHostState,
    val onSnackbarDismissed: () -> Unit,
    val snackbarMessage: String,
    val actionLabel: String,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

class SwipeableConfig(
    val enableDismissFromStartToEnd: Boolean = true,
    val enableDismissFromEndToStart: Boolean = true,
    val gesturesEnabled: Boolean = true,
)