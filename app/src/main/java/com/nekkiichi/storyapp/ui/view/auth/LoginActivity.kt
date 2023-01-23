package com.nekkiichi.storyapp.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.databinding.ActivityLoginBinding
import com.nekkiichi.storyapp.ui.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setup binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginStatus.collect {
                        collectLoginState(it)
                    }
                }
                launch {
                    viewModel.tokenStatus.collect {
                        collectTokenState(it)
                    }
                }
            }
        }

        binding.btnToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(binding.btnLogin, "confirm"),
                Pair(binding.btnToRegister, "switch")
            )
            startActivity(intent,optionsCompat.toBundle())
        }
        binding.btnLogin.setOnClickListener {
            //TODO: Add Loading State and intent listener to main app
            viewModel.logIn(binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString())
        }
    }
    private fun collectLoginState(status: ResponseStatus<FullAuthResponse>) {
        when(status) {
            is ResponseStatus.Loading -> showLoading(true)
            is ResponseStatus.Error -> {
                showLoading(false)
                Toast.makeText(this, "Error: ${status.error}", Toast.LENGTH_SHORT).show()
            }
            else -> showLoading(false)
        }
    }
    private fun collectTokenState(data: ResponseStatus<ListStoryResponse>) {
        when (data) {
            is ResponseStatus.Loading ->{
                Log.d(TAG, "loading state")
                showLoading(true)
            }
            is ResponseStatus.Success -> {
                showLoading(false)
                Log.d(TAG, "token valid")
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            is ResponseStatus.TokenInvalid -> {
                showLoading(false)
                Toast.makeText(this, "Error: session invalid", Toast.LENGTH_SHORT).show()
            }
            else -> {
                showLoading(false)
                Log.d(TAG, "token invalid, enable login activity")
            }
        }
    }
    private fun showLoading(bool: Boolean) {
        if(bool) {
            binding.btnLogin.isEnabled = false
            binding.btnToRegister.isEnabled = false
        }else {
            binding.btnLogin.isEnabled = true
            binding.btnToRegister.isEnabled = true
        }
    }
    companion object {
        val TAG = "LoginActivity"
    }

}