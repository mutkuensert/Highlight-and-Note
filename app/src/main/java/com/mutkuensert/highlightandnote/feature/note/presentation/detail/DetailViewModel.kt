package com.mutkuensert.highlightandnote.feature.note.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mutkuensert.highlightandnote.core.Navigator
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<DetailRoute>()
    private val id: Int? get() = route.id

    private val _uiModel = MutableStateFlow(DetailUiModel.initial())
    val uiModel = _uiModel.asStateFlow()

    private val history: MutableList<String> = mutableListOf()

    init {
        listenAndKeepChangesInHistory()
    }

    fun initScreen() {
        getNote()
    }

    private fun getNote() {
        viewModelScope.launch {
            if (id != null) {
                repository.getNote(id!!).onSuccess { text ->
                    var noteAndHighlightedText = text

                    if (route.highlightedText != null) {
                        noteAndHighlightedText += "\n\n${route.highlightedText}"
                    }

                    _uiModel.update { it.copy(text = noteAndHighlightedText) }
                }
            } else if (route.highlightedText != null) {
                _uiModel.update { it.copy(text = route.highlightedText) }
            }
        }
    }

    fun handleUndoClick() {
        if (history.size < 2) {
            return
        }

        _uiModel.update { it.copy(text = history[history.lastIndex - 1]) }
        history.removeAt(history.lastIndex)
        history.removeAt(history.lastIndex) // 2 times because uiModel is being listened and changes added in history after updating uiModel
    }

    private fun listenAndKeepChangesInHistory() {
        viewModelScope.launch {
            uiModel.drop(1) //Drop initial ui model
                .debounce(500)
                .collect {
                    history.add(it.text)
                }
        }
    }

    fun handleDeleteClick() {
        viewModelScope.launch {
            if (id != null) {
                repository.deleteNote(id!!)
            }

            if (route.highlightedText != null) {
                navigator.closeApp()
            } else {
                navigator.controller.popBackStack()
            }
        }
    }

    fun handleTextChange(text: String) {
        _uiModel.update { it.copy(text = text) }
    }

    fun handleBackClick() {
        viewModelScope.launch {
            if (id != null) {
                updateNote()
            } else {
                saveNewNote()
            }

            if (route.highlightedText != null) {
                navigator.closeApp()
            } else {
                navigator.controller.popBackStack()
            }
        }
    }

    private fun updateNote() {
        viewModelScope.launch {
            repository.updateNote(id!!, uiModel.value.text)
        }
    }

    private fun saveNewNote() {
        viewModelScope.launch {
            repository.saveNewNote(uiModel.value.text)
        }
    }
}