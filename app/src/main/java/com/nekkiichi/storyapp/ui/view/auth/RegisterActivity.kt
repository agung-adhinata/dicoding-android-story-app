package com.nekkiichi.storyapp.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //setup lifecycle
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.registerResponse.collect {
                        collectRegisterState(it)
                    }
                }
            }
        }
        //setup binding
        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(binding.btnRegister, "confirm"),
                Pair(binding.btnToLogin, "switch")
            )
            startActivity(intent, optionsCompat.toBundle())
        }
        binding.btnRegister.setOnClickListener {
            if (isInputValid()) {
                viewModel.createNewAccount(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString()
                )
            }
        }
    }

    private fun collectRegisterState(response: ResponseStatus<BasicResponse>) {
        when (response) {
            is ResponseStatus.Loading -> showLoading(true)
            is ResponseStatus.Error -> {
                showLoading(false)
                Toast.makeText(this, "Error Retrieve data, ${response.error}", Toast.LENGTH_SHORT)
                    .show()
            }
            is ResponseStatus.Success -> {
                showLoading(false)
                Toast.makeText(this, "Done, ${response.data.message}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
            else -> showLoading(false)
        }
    }

    private fun showLoading(bool: Boolean) {
        if (bool) {
            binding.btnRegister.isEnabled = false
            binding.btnToLogin.isEnabled = false
        } else {
            binding.btnRegister.isEnabled = true
            binding.btnToLogin.isEnabled = true
        }
    }

    private fun isInputValid(): Boolean {
        return binding.edRegisterName.text != null && binding.edRegisterEmail.isInputValid && binding.edRegisterPassword.isInputValid
    }

    companion object {
        val TAG = this::class.java.simpleName.toString()
    }
}