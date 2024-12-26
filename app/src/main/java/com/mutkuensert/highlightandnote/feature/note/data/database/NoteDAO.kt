package com.mutkuensert.highlightandnote.feature.note.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {

    @Query("SELECT * FROM noteclass ORDER BY uid DESC")
    suspend fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM noteclass WHERE uid IN (:noteId)")
    suspend fun get(noteId: Int): NoteEntity

    @Insert
    suspend fun insert(vararg notes: NoteEntity)

    @Update
    suspend fun update(vararg note: NoteEntity)

    @Query("DELETE FROM noteclass WHERE uid=:id")
    suspend fun delete(id: Int)
}