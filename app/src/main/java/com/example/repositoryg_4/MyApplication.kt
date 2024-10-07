package com.example.repositoryg_4

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlin.io.resolve

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStoreManager.dataStore = PreferenceDataStoreFactory.create(
            produceFile = { filesDir.resolve("user_prefs.preferences_pb") }
        )
    }
}