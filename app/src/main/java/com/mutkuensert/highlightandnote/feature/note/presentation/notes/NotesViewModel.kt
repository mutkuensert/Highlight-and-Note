package com.mutkuensert.highlightandnote.feature.note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.core.AppNavigator
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import com.mutkuensert.highlightandnote.feature.note.presentation.detail.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val _notes = MutableStateFlow<List<NoteUiModel>>(emptyList())
    val notes = _notes.asStateFlow().shortenLongNotes()

    fun initScreen() {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            _notes.update { repository.getNotes().map { NoteUiModel(it.id, it.text) } }
        }
    }

    fun handleOnClickNote(id: Int, selectedTextInIntent: String?) {
        appNavigator.controller.navigate(
            DetailRoute(
                id = id,
                text = selectedTextInIntent
            )
        )
    }

    fun handleOnClickNewNote() {
        appNavigator.controller.navigate(DetailRoute())
    }

    fun handleDeleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id)
            _notes.update { currentNotes ->
                val newNotes = currentNotes.toMutableList()
                val itemIndex = currentNotes.indexOfFirst { it.id == id }
                newNotes.drop(itemIndex)
                newNotes
            }
        }
    }

    private fun StateFlow<List<NoteUiModel>>.shortenLongNotes(): StateFlow<List<NoteUiModel>> {
        return map { notes ->
            notes.map { note ->
                if (note.text.length > 300) {
                    val notePreview = note.text.substring(0..300) + "..."
                    note.copy(text = notePreview)
                } else {
                    note
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }
}