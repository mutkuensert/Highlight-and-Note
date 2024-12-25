package com.mutkuensert.highlightandnote.feature.note.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope

@Composable
fun RepeatOnLifecycleEffect(
    vararg keys: Any?,
    state: Lifecycle.State = Lifecycle.State.RESUMED,
    action: CoroutineScope.() -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(keys) {
        lifecycle.repeatOnLifecycle(state, block = action)
    }
}