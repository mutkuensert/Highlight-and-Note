package com.mutkuensert.highlightandnote.core

import androidx.navigation.NavHostController
import com.mutkuensert.highlightandnote.main.MainActivity
import javax.inject.Inject

class Navigator @Inject constructor() {
    lateinit var controller: NavHostController
        private set
    private lateinit var activity: MainActivity

    fun setNavHostController(controller: NavHostController) {
        this.controller = controller
    }

    fun setActivity(activity: MainActivity) {
        this.activity = activity
    }

    fun closeApp() {
        activity.finish()
    }
}