package com.mutkuensert.highlightandnote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteClass(
    @ColumnInfo(name = "note")
    val note: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}
