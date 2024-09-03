package com.mutkuensert.highlightandnote.feature.note.domain

interface NoteRepository {

    suspend fun getNotes(): List<Note>

    suspend fun updateNote(id: Int, text: String)

    suspend fun deleteNote(id: Int)

    suspend fun saveNewNote(text: String)

    suspend fun getNote(id: Int): Result<String>
}
