package com.mutkuensert.highlightandnote.feature.note.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    onNavigateToNote: (id: Int) -> Unit,
    onNavigateToNewNote: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Home(notes, onNavigateToNote, onNavigateToNewNote)

    LaunchedEffect(Unit) {
        viewModel.getNotes()
    }
}

@Composable
private fun Home(
    notes: List<NoteUiModel>,
    onClickNote: (id: Int) -> Unit,
    onClickNewNote: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Header(onClickNewNote)

        Notes(notes, onClickNote)
    }
}

@Composable
private fun Notes(
    notes: List<NoteUiModel>,
    onClickNote: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(notes.size) {
            val note = notes[it]

            Note(
                onClick = { onClickNote.invoke(note.id) },
                text = note.text
            )

            if (it != notes.lastIndex) {
                Spacer(Modifier.height(8.dp))
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
private fun Note(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text
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

    Home(fakeNotes, {}, {})
}