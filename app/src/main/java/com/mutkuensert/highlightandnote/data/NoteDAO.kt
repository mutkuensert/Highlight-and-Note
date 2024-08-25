package com.mutkuensert.highlightandnote.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {

    @Query("SELECT * FROM noteclass ORDER BY uid DESC")
    suspend fun getAll(): List<NoteClass>

    @Query("SELECT * FROM noteclass WHERE uid IN (:noteId)")
    suspend fun loadById(noteId: Int): NoteClass

    @Insert
    suspend fun insert(vararg notes: NoteClass)

    @Update
    suspend fun update(vararg note: NoteClass)

    @Delete
    suspend fun delete(uid: Int)
}