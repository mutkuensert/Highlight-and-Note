package com.mutkuensert.highlightandnote.feature.note.core

import androidx.navigation.NavHostController
import com.mutkuensert.highlightandnote.main.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() {
    lateinit var controller: NavHostController
        private set
    private lateinit var activity: MainActivity

    fun setNavHostController(controller: NavHostController) {
        this.controller = controller
    }

    fun setActivity(activity: MainActivity) {
        this.activity = activity
    }

    fun finish() {
        activity.finish()
    }
}