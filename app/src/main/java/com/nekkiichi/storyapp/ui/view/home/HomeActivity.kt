package com.nekkiichi.storyapp.ui.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nekkiichi.storyapp.adapter.StoryListAdapter
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.databinding.ActivityHomeBinding
import com.nekkiichi.storyapp.ui.view.addStory.AddStoryActivity
import dagger.hilt.android.AndroidEntryPoint
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
                        collectStoryListResponse(it)
                    }
                }
            }
        }

        //setup listener
        binding.fab.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }
    private fun collectStoryListResponse(status: ResponseStatus<ListStoryResponse>) {
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
    fun updateRecycleView(data: List<StoryItem>?) {
        if(data == null) {
            showEmptyList()
        }else {
            val layoutManager = LinearLayoutManager(this)
            binding.rvStoryList.layoutManager = layoutManager
            val storyListAdapter = StoryListAdapter(data, this)
            val dividerItemDecoration = DividerItemDecoration(this,layoutManager.orientation)
            binding.rvStoryList.adapter = storyListAdapter
            binding.rvStoryList.addItemDecoration(dividerItemDecoration)
        }
    }
    fun showLoading(status: Boolean) {

    }
    fun showEmptyList() {

    }
}