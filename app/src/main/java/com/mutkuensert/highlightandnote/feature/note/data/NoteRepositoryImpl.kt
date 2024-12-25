package com.mutkuensert.highlightandnote.feature.note.data

import com.mutkuensert.highlightandnote.feature.note.data.database.NoteClass
import com.mutkuensert.highlightandnote.feature.note.data.database.NoteDAO
import com.mutkuensert.highlightandnote.feature.note.domain.Note
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDAO,
) : NoteRepository {

    override suspend fun getNotes(): List<Note> {
        return dao.getAll().map { Note(it.uid, it.note ?: "") }
    }

    override suspend fun getNote(id: Int): Result<String> {
        val note = dao.get(id).note
        return if (note != null) {
            Result.success(note)
        } else {
            Result.failure(Exception("Note couldn't be found."))
        }
    }

    override suspend fun updateNote(id: Int, text: String) {
        val note = NoteClass(note = text)
        note.uid = id
        dao.update(note)
    }

    override suspend fun deleteNote(id: Int) {
        dao.delete(id)
    }

    override suspend fun saveNewNote(text: String) {
        dao.insert(NoteClass(text))
    }
}