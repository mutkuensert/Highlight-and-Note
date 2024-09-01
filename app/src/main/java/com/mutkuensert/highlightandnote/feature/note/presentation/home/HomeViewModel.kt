package com.mutkuensert.highlightandnote.feature.note.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {
    private val _notes = MutableStateFlow<List<NoteUiModel>>(emptyList())
    val notes = _notes.asStateFlow().shortenLongNotes()

    fun initScreen() {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.update { repository.getNotes().map { NoteUiModel(it.id, it.text) } }
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