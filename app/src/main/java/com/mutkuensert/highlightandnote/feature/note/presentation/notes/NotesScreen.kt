package com.mutkuensert.highlightandnote.feature.note.presentation.notes

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mutkuensert.highlightandnote.feature.note.core.compose.RepeatOnLifecycleEffect
import com.mutkuensert.highlightandnote.feature.note.core.compose.Swipeable
import com.mutkuensert.highlightandnote.theme.appColors

@Composable
fun NotesScreen(
    selectedTextInIntent: String?,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Notes(
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
private fun Notes(
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

            Swipeable(
                snackbarHostState,
                { onSnackbarDismissed.invoke(note.id) },
                backgroundContent = { SwipedBackground() }
            ) {
                Note(
                    onClick = { onClickNote.invoke(note.id) },
                    text = note.text
                )
            }
        }
    }
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
private fun NotesPreview() {
    val fakeNotes = listOf(
        NoteUiModel(
            0,
            "First note first note first note First note first note first note " +
                    "First note first note first note First note first note first note"
        ),
        NoteUiModel(1, "First note")
    )

    Notes(fakeNotes, {}, {}, {})
}