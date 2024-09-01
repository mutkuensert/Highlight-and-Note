package com.mutkuensert.highlightandnote.feature.note.presentation.detail

data class DetailUiModel(
    val text: String,
    val shouldShowDeleteButton: Boolean,
) {

    companion object {

        fun initial(text: String = ""): DetailUiModel {
            return DetailUiModel(text = text, shouldShowDeleteButton = false)
        }
    }
}
