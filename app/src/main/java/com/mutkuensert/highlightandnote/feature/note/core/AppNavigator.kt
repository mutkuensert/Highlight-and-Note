package com.mutkuensert.highlightandnote.feature.note.core

import androidx.navigation.NavHostController

class AppNavigator {
    lateinit var controller: NavHostController
        private set

    fun setNavHostController(controller: NavHostController) {
        this.controller = controller
    }
}