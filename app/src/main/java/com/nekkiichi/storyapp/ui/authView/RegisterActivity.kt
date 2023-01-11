package com.nekkiichi.storyapp.ui.authView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
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
        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            if(isInputValid()) {
                viewModel.createNewAccount(binding.edRegisterName.text.toString(), binding.edRegisterEmail.text.toString(), binding.edRegisterPassword.text.toString())
            }
        }
    }

    fun collectRegisterState(response: ResponseStatus<BasicResponse>) {
        when (response) {
            is ResponseStatus.loading -> showLoading(true)
            is ResponseStatus.Error -> {
                showLoading(true)
                Toast.makeText(this, "Error Retrieve data, ${response.error}", Toast.LENGTH_SHORT).show()
            }
            is ResponseStatus.Success ->{
                showLoading(true)
                Toast.makeText(this, "Done, ${response.data.message}", Toast.LENGTH_SHORT).show()
            }
            else-> {}
        }
    }
    private fun showLoading(status: Boolean) {

    }
    private fun isInputValid():Boolean {
        return binding.edRegisterName.text != null && binding.edRegisterEmail.isInputValid && binding.edRegisterPassword.isInputValid
    }
    companion object {
        val TAG = this::class.java.simpleName.toString()
    }
}