package com.mutkuensert.highlightandnote.main

import com.mutkuensert.highlightandnote.core.AppNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {

    @Provides
    @ActivityRetainedScoped
    fun provideAppNavigator() = AppNavigator()
}