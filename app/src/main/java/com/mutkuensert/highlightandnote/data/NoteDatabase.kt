package com.mutkuensert.highlightandnote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mutkuensert.highlightandnote.model.NoteClass

@Database(entities = [NoteClass::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO
}