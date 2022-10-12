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
    val processIsDone = MutableLiveData<Boolean>(false)
    var noteBackupForActivityDestruction: String? = null

    fun createDataAccessObject(context: Context){
        dao = NoteDatabase(context).noteDao()
    }


    fun deleteNote(note: NoteClass){
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(note)
            processIsDone.postValue(true)
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
            processIsDone.postValue(true)
        }
    }

    fun updateNote(note : NoteClass){
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(note)
            processIsDone.postValue(true)
        }
    }

}