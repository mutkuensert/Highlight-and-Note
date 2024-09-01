package com.mutkuensert.highlightandnote.feature.note.presentation.detail

data class DetailUiModel(
    val text: String,
) {

    companion object {

        fun initial(text: String = ""): DetailUiModel {
            return DetailUiModel(text = text)
        }
    }
}
