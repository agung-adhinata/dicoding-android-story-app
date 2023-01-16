package com.nekkiichi.storyapp.ui.view.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.ui.view.auth.LoginActivity
import com.nekkiichi.storyapp.ui.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val viewModel: StartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        //setup binding
        setContentView(R.layout.activity_start)
        // setup lifecycle
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLogin.collect {
                        collectLoginState(it)
                    }
                }
            }
        }
    }
    private fun collectLoginState(data: ResponseStatus<ListStoryResponse>) {
        when (data) {
            is ResponseStatus.loading ->{
                Log.d(TAG, "loading state")
            }
            is ResponseStatus.Success -> {
                Log.d(TAG, "token valid")
                val intent = Intent(this@StartActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Log.d(TAG, "token invalid")
                val intent = Intent(this@StartActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
        }
    }
    companion object {
        const val TAG = "StartActivity"
    }
}