package com.mutkuensert.highlightandnote.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutkuensert.highlightandnote.model.NoteClass

@Composable
fun HomeScreen(
    onNavigateToNote: (id: Int) -> Unit,
    selectedText: String? = null,
    viewModel: HomeViewModel = viewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Home(notes, onNavigateToNote)

    LaunchedEffect(Unit) {
        viewModel.getNotes()
    }
}

@Composable
private fun Home(
    notes: List<NoteClass>,
    onClickNote: (id: Int) -> Unit
) {
    LazyColumn {
        items(notes.size) {
            val note = notes[it]

            if (note.note != null) {
                Text(
                    modifier = Modifier.clickable { onClickNote.invoke(note.uid) },
                    text = note.note
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    val fakeNotes = listOf(
        NoteClass("First note"),
        NoteClass("First note")
    )

    Home(fakeNotes, {})
}