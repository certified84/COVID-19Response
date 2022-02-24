package com.certified.covid19response.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.certified.covid19response.R
import com.certified.covid19response.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun onBackPressed() {
        val navigationController =
            Navigation.findNavController(this, R.id.nav_host_fragment_container)
        if (navigationController.currentDestination?.id == R.id.homeFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}