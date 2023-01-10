package com.nekkiichi.storyapp.ui.AuthActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}