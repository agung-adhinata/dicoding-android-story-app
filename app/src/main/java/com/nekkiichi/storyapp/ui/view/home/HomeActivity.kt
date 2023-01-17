package com.nekkiichi.storyapp.ui.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.adapter.StoryListAdapter
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.databinding.ActivityHomeBinding
import com.nekkiichi.storyapp.ui.view.addStory.AddStoryActivity
import com.nekkiichi.storyapp.ui.view.auth.LoginActivity
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
        title = "Home"
        //setup observer
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.storyList.collect {
                        collectStoryListResponse(it)
                    }
                }
                launch {
                    viewModel.getALlStories()
                }
            }
        }

        //setup listener
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_logout -> {
                viewModel.logOut()
                true
            }
            else -> false
        }
    }
    private fun collectStoryListResponse(status: ResponseStatus<ListStoryResponse>) {
        when(status) {
            is ResponseStatus.Error -> {
                Toast.makeText(this, "Error fetch data: ${status.error}", Toast.LENGTH_SHORT).show()
            }
            is ResponseStatus.TokenInvalid -> {
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
            is ResponseStatus.Success -> {
                if(viewModel.prevStoryList.value != status.data) {
                    Log.d(TAG, "Data Updated")
                    viewModel.prevStoryList.value = status.data
                    updateRecycleView(status.data.listStory)
                }else{
                    Log.d(TAG, "Data Reserved")
                    if(binding.rvStoryList.adapter == null) {
                        updateRecycleView(status.data.listStory)
                    }
                }
            }
            else-> {} //do nothing
        }
    }
    private fun updateRecycleView(data: List<StoryItem>?) {
        if(data == null) {
            Toast.makeText(this, "data empty", Toast.LENGTH_SHORT).show()
        }else {
            val layoutManager = LinearLayoutManager(this)
            binding.rvStoryList.layoutManager = layoutManager
            val storyListAdapter = StoryListAdapter(data, this)
            val dividerItemDecoration = DividerItemDecoration(this,layoutManager.orientation)
            binding.rvStoryList.adapter = storyListAdapter
            binding.rvStoryList.addItemDecoration(dividerItemDecoration)
        }
    }
    companion object {
        val TAG = this::class.java.simpleName
    }
}