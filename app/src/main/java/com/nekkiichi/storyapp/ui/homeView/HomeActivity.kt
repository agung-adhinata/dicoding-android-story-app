package com.nekkiichi.storyapp.ui.homeView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}