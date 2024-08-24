package com.mutkuensert.highlightandnote.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.data.NoteDAO
import com.mutkuensert.highlightandnote.model.NoteClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val dao: NoteDAO) : ViewModel() {
    private val _notes = MutableStateFlow<List<NoteClass>>(emptyList())
    val notes = _notes.asStateFlow()

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.update { dao.getAll() }
        }
    }
}