package com.certified.covid19response.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.certified.covid19response.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkModeEnabled()
    }

    private fun isDarkModeEnabled() {
//        val darkModePreference = getString(R.string.key_theme)
//        val darkModeValues = resources.getStringArray(R.array.pref_theme_values)
//        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
//        when (preferences.getString(darkModePreference, darkModeValues[0])) {
//            darkModeValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            darkModeValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            darkModeValues[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}