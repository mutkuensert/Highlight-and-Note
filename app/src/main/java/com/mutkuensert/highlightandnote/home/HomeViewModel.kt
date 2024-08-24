package com.mutkuensert.highlightandnote.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.service.NoteDAO
import com.mutkuensert.highlightandnote.service.NoteDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<NoteClass>>(emptyList())
    val notes: StateFlow<List<NoteClass>> get() = _notes

    private lateinit var dao: NoteDAO

    fun initScreen(context: Context) {
        createDataAccessObject(context)
        getNotes()
    }

    private fun createDataAccessObject(context: Context) {
        dao = NoteDatabase(context).noteDao()
    }

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.update { dao.getAll() }
        }
    }
}