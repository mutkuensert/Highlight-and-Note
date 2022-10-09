package com.mutkuensert.highlightandnote.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.service.NoteDAO
import com.mutkuensert.highlightandnote.service.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragmentViewModel: ViewModel() {
    lateinit var dao: NoteDAO
    val note = MutableLiveData<NoteClass>()

    fun createDataAccessObject(context: Context){
        dao = NoteDatabase(context).noteDao()
    }


    fun deleteNote(note: NoteClass){
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(note)
        }
    }
    fun getNote(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            note.postValue(dao.loadById(id))
        }
    }
    fun newNote(note : NoteClass){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(note)
        }
    }

    fun updateNote(note : NoteClass){
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(note)
        }
    }

}