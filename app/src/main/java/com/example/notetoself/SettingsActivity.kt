package com.example.notetoself

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.widget.Switch

class SettingsActivity : AppCompatActivity() {
    private var showDividers: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val prefs = getSharedPreferences(
                "Note to self",
                Context.MODE_PRIVATE
        )
        showDividers  = prefs.getBoolean("dividers", true)
        val switch1= findViewById<Switch>(R.id.switch1)
        // Set the switch on or off as appropriate
        switch1.isChecked = showDividers
        switch1.setOnCheckedChangeListener {
            buttonView, isChecked ->
            showDividers = isChecked
        }

    }
    override fun onPause() {
        super.onPause()
       // Save the settings here
       val prefs = getSharedPreferences(
               "Note to self",
               Context.MODE_PRIVATE
       )
        val editor = prefs.edit()
        editor.putBoolean("dividers", showDividers)
        editor.apply()
    }
}