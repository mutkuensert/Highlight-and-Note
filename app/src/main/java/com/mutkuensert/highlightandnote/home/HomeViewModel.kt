package com.mutkuensert.highlightandnote.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.data.NoteClass
import com.mutkuensert.highlightandnote.data.NoteDAO
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
class HomeViewModel @Inject constructor(private val dao: NoteDAO) : ViewModel() {
    private val _notes = MutableStateFlow<List<NoteClass>>(emptyList())
    val notes: StateFlow<List<NoteClass>> = _notes.asStateFlow().shortenLongNotes()

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.update { dao.getAll() }
        }
    }

    private fun StateFlow<List<NoteClass>>.shortenLongNotes(): StateFlow<List<NoteClass>> {
        return map { notes ->
            notes.map { note ->
                if (note.note != null && note.note.length > 300) {
                    val notePreview = note.note.substring(0..300) + "..."
                    note.copy(note = notePreview)
                } else {
                    note
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }
}