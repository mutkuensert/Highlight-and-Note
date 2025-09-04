package com.mutkuensert.highlightandnote.feature.note.presentation.detail

import kotlinx.serialization.Serializable

@Serializable
data class DetailNavArgs(
    val id: Int? = null,
    val receivedHighlightedText: String? = null
)
