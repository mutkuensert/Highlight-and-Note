package com.mutkuensert.highlightandnote.service

import androidx.room.*
import com.mutkuensert.highlightandnote.model.NoteClass

@Dao
interface NoteDAO {

    @Query("SELECT * FROM noteclass ORDER BY uid DESC")
    suspend fun getAll(): List<NoteClass>

    @Query("SELECT * FROM noteclass WHERE uid IN (:noteId)")
    suspend fun loadById(noteId: Int) : NoteClass

    @Insert
    suspend fun insert(vararg notes : NoteClass)

    @Update
    suspend fun update(vararg note : NoteClass)

    @Delete
    suspend fun delete(note : NoteClass)
}