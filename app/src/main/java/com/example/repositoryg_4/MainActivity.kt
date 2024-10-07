package com.example.repositoryg_4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

object DataStoreManager
{
    lateinit var dataStore: DataStore<Preferences>
}

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.theme_spinner)
        val options = resources.getStringArray(R.array.theme_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter

        val inputTextField = findViewById<TextInputEditText>(R.id.txt_input)
        val saveTextButton = findViewById<Button>(R.id.btn_saveText)

        val goTo2ndActivity = findViewById<Button>(R.id.btn_goTo2nd)
        goTo2ndActivity.setOnClickListener{
            startActivity(Intent(this, ReadTextActivity::class.java))
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        // Set light theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                    1 -> {
                        // Set dark theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
        val initialPosition = when (defaultNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> 1 // Dark theme
            else -> 0 // Light theme
        }
        spinner.setSelection(initialPosition)

        // Save the input value to DataStore
        saveTextButton.setOnClickListener {
            lifecycleScope.launch {
                val inputText = inputTextField.text.toString()
                DataStoreManager.dataStore.edit { preferences ->
                    preferences[stringPreferencesKey("input_text")] = inputText
                }
            }
        }

        // Read the value from DataStore and display it in the TextInputEditText
        lifecycleScope.launch {
            DataStoreManager.dataStore.data.collect { preferences ->
                val savedText = preferences[stringPreferencesKey("input_text")] ?: ""
                inputTextField.setText(savedText)
            }
        }
    }
}