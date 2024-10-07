package com.example.repositoryg_4

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isNotEmpty
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import kotlinx.coroutines.launch

class ReadTextActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_read_text)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        val storageText = findViewById<TextView>(R.id.txt_storage)
        val readStorageButton = findViewById<Button>(R.id.btn_readStorage)

        backButton.setOnClickListener {
            finish()
        }

        readStorageButton.setOnClickListener {
            lifecycleScope.launch {
                DataStoreManager.dataStore.data.collect { preferences ->
                    val savedText = preferences[stringPreferencesKey("input_text")] ?: ""

                    if (savedText.isNotEmpty())
                    {
                        storageText.text = savedText
                    }
                    else
                    {
                        Toast.makeText(this@ReadTextActivity, "No text stored", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}