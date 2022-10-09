package com.mutkuensert.highlightandnote.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.service.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragmentViewModel(application: Application): BaseViewModel(application) {
    val dao = NoteDatabase(application).noteDao()
    val note = MutableLiveData<NoteClass>()


    fun kayitSil(not: NoteClass){
        launch {
            dao.delete(not)
        }
    }
    fun kayitGetir(id: Int){
        launch {
            note.value =dao.loadById(id)
        }
    }
    fun yeniKayit(not : NoteClass){
        launch {
            dao.insert(not)
        }
    }

    fun kayitGuncelle(not : NoteClass){
        launch {
            dao.guncelle(not)
        }
    }

}