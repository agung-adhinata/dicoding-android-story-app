package com.nekkiichi.storyapp.ui.AuthActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nekkiichi.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel:AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}