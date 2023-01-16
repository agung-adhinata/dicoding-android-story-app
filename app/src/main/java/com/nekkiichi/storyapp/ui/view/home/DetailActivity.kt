package com.nekkiichi.storyapp.ui.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val data = intent.getParcelableExtra<StoryItem>(EXTRA_STORY) as StoryItem
        binding.tvDetailName.text = data.name
        binding.tvDetailDescription.text = data.description
        Glide.with(this).load(data.photoUrl).thumbnail().apply(
            RequestOptions.bitmapTransform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.roundedIvSize
                    )
                )
            )
        ).into(binding.tvDetailPhoto)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}