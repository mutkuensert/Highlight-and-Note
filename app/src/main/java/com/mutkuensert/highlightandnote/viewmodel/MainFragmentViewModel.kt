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

class MainFragmentViewModel: ViewModel() {
    val notes = MutableLiveData<List<NoteClass>>()
    lateinit var dao: NoteDAO

    fun createDataAccessObject(context: Context){
        dao = NoteDatabase(context).noteDao()
    }

    fun getNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            notes.postValue(dao.getAll())
        }
    }

}