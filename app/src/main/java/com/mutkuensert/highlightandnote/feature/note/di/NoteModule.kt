package com.mutkuensert.highlightandnote.feature.note.di

import android.content.Context
import androidx.room.Room
import com.mutkuensert.highlightandnote.feature.note.data.NoteRepositoryImpl
import com.mutkuensert.highlightandnote.feature.note.data.database.NoteDAO
import com.mutkuensert.highlightandnote.feature.note.data.database.NoteDatabase
import com.mutkuensert.highlightandnote.feature.note.domain.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {

    @Binds
    @Singleton
    fun bindNoteRepository(implementation: NoteRepositoryImpl): NoteRepository

    companion object {

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
}