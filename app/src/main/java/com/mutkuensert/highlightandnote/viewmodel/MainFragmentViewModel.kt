package com.mutkuensert.highlightandnote.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.service.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentViewModel(application: Application) : BaseViewModel(application) {
    val notes = MutableLiveData<List<NoteClass>>()
    val dao = NoteDatabase(application).noteDao()


    fun notlariGetir (){
        launch {
            notes.value = dao.getAll()
        }
    }

}