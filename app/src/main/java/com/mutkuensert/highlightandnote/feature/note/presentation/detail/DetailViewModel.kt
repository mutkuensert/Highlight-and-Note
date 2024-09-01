package com.mutkuensert.highlightandnote.feature.note.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<DetailRoute>()
    private val id: Int? get() = route.id

    private val _uiModel = MutableStateFlow(DetailUiModel.initial(route.text ?: ""))
    val uiModel = _uiModel.asStateFlow()

    private val history: MutableList<String> = mutableListOf()

    init {
        listenAndKeepChangesInHistory()
    }

    fun initScreen() {
        if (id != null) {
            getNote()
        }
    }

    private fun getNote() {
        launchInIo {
            repository.getNote(id!!).onSuccess { text ->
                var textWithAdditionalText = text

                if (route.text != null) {
                    textWithAdditionalText = "$text\n\n${route.text}"
                }

                _uiModel.update { it.copy(text = textWithAdditionalText) }
            }
        }
    }

    fun handleUndoClick() {
        if (history.size < 2) {
            return
        }

        _uiModel.update { it.copy(text = history[history.lastIndex - 1]) }
        history.removeLast()
        history.removeLast() // 2 times because uiModel is being listened and changes added in history after updating uiModel
    }

    private fun listenAndKeepChangesInHistory() {
        launchInIo {
            uiModel.debounce(500).collectLatest {
                history.add(it.text)
            }
        }
    }

    fun handleDeleteClick() {
        launchInIo {
            repository.deleteNote(id!!)
        }
    }

    fun handleTextChange(text: String) {
        _uiModel.update { it.copy(text = text) }
    }

    fun handleBackClick(onFinishApp: () -> Unit) {
        if (route.text != null) {
            onFinishApp.invoke()
        }

        launchInIo {
            if (id != null) {
                updateNote()
            } else {
                saveNewNote()
            }
        }
    }

    private fun updateNote() {
        launchInIo {
            repository.updateNote(id!!, uiModel.value.text)
        }
    }

    private fun saveNewNote() {
        launchInIo {
            repository.saveNewNote(uiModel.value.text)
        }
    }

    private fun launchInIo(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }
}