package com.mutkuensert.highlightandnote.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.data.NoteDAO
import com.mutkuensert.highlightandnote.data.NoteClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(private val dao: NoteDAO) : ViewModel() {
    private val _uiModel = MutableStateFlow("")
    val uiModel = _uiModel.asStateFlow()

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(id)
        }
    }

    fun getNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiModel.update { dao.loadById(id).note ?: "" }
        }
    }

    fun saveNewNote(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(NoteClass(text))
        }
    }

    fun updateNote(id: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(NoteClass(text).apply { uid = id })
        }
    }
}