package com.nekkiichi.storyapp.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.adapter.StoryListAdapter
import com.nekkiichi.storyapp.databinding.ActivityHomeBinding
import com.nekkiichi.storyapp.ui.view.addStory.AddStoryActivity
import com.nekkiichi.storyapp.ui.view.auth.LoginActivity
import com.nekkiichi.storyapp.ui.view.homeMaps.HomeMapsActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pagingDataAdapter: StoryListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setup binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Home"

        pagingDataAdapter = StoryListAdapter(StoryListAdapter.StoryComparator)
        updateRecycleView()
        pagingDataAdapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            when (errorState?.error) {
                is IOException -> {}
                is HttpException -> {}
                is ArithmeticException -> {
                    Toast.makeText(this, "token invalid", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
        //setup observer
        viewModel.stories.observe(this) {
            pagingDataAdapter.submitData(lifecycle, it)
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
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logOut()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
                true
            }
            R.id.action_open_map -> {
                startActivity(Intent(this@HomeActivity, HomeMapsActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        pagingDataAdapter.refresh()
    }
    private fun updateRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStoryList.adapter = pagingDataAdapter
        binding.rvStoryList.addItemDecoration(dividerItemDecoration)
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}