package com.mutkuensert.highlightandnote.feature.note.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoteClass")
data class NoteEntity(
    @ColumnInfo(name = "note")
    val note: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
