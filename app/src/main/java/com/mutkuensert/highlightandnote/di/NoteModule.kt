package com.mutkuensert.highlightandnote.di

import android.content.Context
import androidx.room.Room
import com.mutkuensert.highlightandnote.data.NoteDAO
import com.mutkuensert.highlightandnote.data.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "notedatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDAO {
        return database.noteDao()
    }
}