package com.mutkuensert.highlightandnote.feature.note.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mutkuensert.highlightandnote.core.AppNavigator
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
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
    private val appNavigator: AppNavigator,
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
        launchInIo {
            if (id != null) {
                repository.getNote(id!!).onSuccess { text ->
                    var textWithAdditionalText = text

                    if (route.text != null) {
                        textWithAdditionalText = "$text\n\n${route.text}"
                    }

                    _uiModel.update { it.copy(text = textWithAdditionalText) }
                }
            } else {
                _uiModel.update { it.copy(text = route.text ?: "") }
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
        launchInIo {
            uiModel.drop(1) //Drop initial ui model
                .debounce(500)
                .collect {
                    history.add(it.text)
                }
        }
    }

    fun handleDeleteClick() {
        launchInIo {
            repository.deleteNote(id!!)
            appNavigator.controller.popBackStack()
        }
    }

    fun handleTextChange(text: String) {
        _uiModel.update { it.copy(text = text) }
    }

    fun handleBackClick() {
        launchInIo {
            if (id != null) {
                updateNote()
            } else {
                saveNewNote()
            }

            if (route.text != null) {
                appNavigator.finish()
            } else {
                appNavigator.controller.popBackStack()
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