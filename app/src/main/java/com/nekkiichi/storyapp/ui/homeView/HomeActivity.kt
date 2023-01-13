package com.nekkiichi.storyapp.ui.homeView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.adapter.StoryListAdapter
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.ListStoryItemResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setup binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setup observer
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.storyList.collect {

                    }
                }
            }
        }
    }
    fun validateStoryListResponse(status: ResponseStatus<ListStoryResponse>) {
        when(status) {
            is ResponseStatus.loading-> showLoading(true)
            is ResponseStatus.Error -> {
                showLoading(false)
                Toast.makeText(this, "Error fatch data: ${status.error}", Toast.LENGTH_SHORT).show()
            }
            is ResponseStatus.Success -> {
                showLoading(false)
                updateRecycleView(status.data.listStory)
            }
            else-> showLoading(false)
        }
    }
    fun updateRecycleView(data: List<ListStoryItemResponse>?) {
        if(data == null) {
            showEmptyList()
        }else {
            binding.rvStoryList.layoutManager = LinearLayoutManager(this)
            val storyListAdapter = StoryListAdapter(data, this)
            binding.rvStoryList.adapter = storyListAdapter
        }
    }
    fun showLoading(status: Boolean) {

    }
    fun showEmptyList() {

    }
}