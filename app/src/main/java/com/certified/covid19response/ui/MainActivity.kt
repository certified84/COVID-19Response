package com.certified.covid19response.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.certified.covid19response.R
import com.certified.covid19response.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}