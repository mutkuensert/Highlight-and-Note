package com.mutkuensert.highlightandnote.feature.note.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mutkuensert.highlightandnote.feature.note.core.RepeatOnLifecycleEffect
import com.mutkuensert.highlightandnote.theme.appColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    selectedTextInIntent: String?,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Home(
        notes = notes,
        onClickNote = { noteId -> viewModel.handleOnClickNote(noteId, selectedTextInIntent) },
        onClickNewNote = viewModel::handleOnClickNewNote,
        onDeleteNote = viewModel::handleDeleteNote
    )

    RepeatOnLifecycleEffect(Unit) {
        viewModel.initScreen()
    }
}


@Composable
private fun Home(
    notes: List<NoteUiModel>,
    onClickNote: (id: Int) -> Unit,
    onClickNewNote: () -> Unit,
    onDeleteNote: (id: Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Header(onClickNewNote)

            Notes(
                notes,
                onClickNote,
                onSnackbarDismissed = { noteId: Int -> onDeleteNote.invoke(noteId) },
                snackbarHostState
            )
        }
    }
}

@Composable
private fun Notes(
    notes: List<NoteUiModel>,
    onClickNote: (id: Int) -> Unit,
    onSnackbarDismissed: (noteId: Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(notes.size, { notes[it].id }) {
            val note = notes[it]
            SwipeableNote(
                note,
                onClick = { onClickNote.invoke(note.id) },
                onSnackbarDismissed,
                snackbarHostState,
                modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun SwipeableNote(
    note: NoteUiModel,
    onClick: () -> Unit,
    onSnackbarDismissed: (noteId: Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val isNoteVisible = remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { dismissValue ->
        handleValueChange(
            dismissValue,
            isNoteVisible,
            coroutineScope,
            snackbarHostState,
            onSnackbarDismissed = {
                onSnackbarDismissed.invoke(note.id)
            }
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
            backgroundContent = { SwipedBackground() },
            modifier,
            enableDismissFromStartToEnd = true,
            enableDismissFromEndToStart = true,
            content = {
                Note(onClick = onClick, text = note.text)
            }
        )
    }
}

private fun handleValueChange(
    dismissValue: SwipeToDismissBoxValue,
    isNoteVisible: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onSnackbarDismissed: () -> Unit,
): Boolean {
    if (dismissValue == SwipeToDismissBoxValue.Settled) {
        return true
    }

    if (!isNoteVisible.value) {
        return false
    }

    coroutineScope.launch {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = "Silme işlemini geri al?",
            actionLabel = "Geri al",
            duration = SnackbarDuration.Short
        )

        if (snackbarResult == SnackbarResult.Dismissed) {
            onSnackbarDismissed.invoke()
        } else {
            isNoteVisible.value = true

        }
    }
    isNoteVisible.value = false
    return true
}

@Composable
private fun Header(onClickNewNote: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))

        NewButton(onClickNewNote)
    }
}

@Composable
private fun NewButton(onClickNewNote: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClickNewNote, modifier = modifier) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
private fun SwipedBackground() {
    Row(
        modifier = Modifier
            .background(color = Color.Red, shape = RoundedCornerShape(8.dp))
            .fillMaxSize()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DeleteIcon()
        DeleteIcon()
    }
}

@Composable
private fun DeleteIcon() {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        tint = Color.White
    )
}

@Composable
private fun Note(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                5.dp,
                MaterialTheme.shapes.medium,
                ambientColor = MaterialTheme.appColors.shadow,
                spotColor = MaterialTheme.appColors.shadow
            )
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun HomePreview() {
    val fakeNotes = listOf(
        NoteUiModel(
            0,
            "First note first note first note First note first note first note " +
                    "First note first note first note First note first note first note"
        ),
        NoteUiModel(1, "First note")
    )

    Home(fakeNotes, {}, {}, {})
}