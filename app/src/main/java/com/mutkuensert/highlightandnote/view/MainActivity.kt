package com.mutkuensert.highlightandnote.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.service.SingletonClass

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.action != null && (intent.action.equals(Intent.ACTION_PROCESS_TEXT) || intent.action.equals(Intent.ACTION_SEND))){
            if(intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)){
                val singleton = SingletonClass.AlinanText
                singleton.alinanText = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                singleton.kontrol = 1
            }else if (intent.hasExtra(Intent.EXTRA_TEXT)){
                val singleton = SingletonClass.AlinanText
                singleton.alinanText = intent.getStringExtra(Intent.EXTRA_TEXT)
                singleton.kontrol = 1
            }
        }else{
            val singleton = SingletonClass.AlinanText
            singleton.kontrol = 0
        }
    }
}