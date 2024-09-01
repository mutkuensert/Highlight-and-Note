package com.mutkuensert.highlightandnote.feature.note.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteClass::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO
}