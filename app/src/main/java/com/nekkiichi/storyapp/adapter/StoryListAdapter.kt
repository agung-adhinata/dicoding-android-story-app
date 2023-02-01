package com.nekkiichi.storyapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.databinding.ItemStorylistBinding
import com.nekkiichi.storyapp.ui.view.home.DetailActivity

class StoryListAdapter(diffCallback: DiffUtil.ItemCallback<StoryItem>) :
    PagingDataAdapter<StoryItem, StoryListAdapter.ListViewHolder>(diffCallback) {
    inner class ListViewHolder(itemView: ItemStorylistBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private lateinit var storyId: String
        private var ivContent = itemView.ivItemPhoto
        private var tvUsername = itemView.tvItemName
        private var tvDescription = itemView.tvContentDescription
        fun bind(data: StoryItem) {
            storyId = data.id
            tvUsername.text = data.name
            tvDescription.text = data.description
            Glide.with(itemView.context).load(data.photoUrl).thumbnail().apply(
                RequestOptions.bitmapTransform(
                    RoundedCorners(
                        20
                    )
                )
            ).into(ivContent)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemStorylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataItem = getItem(position)
        if(dataItem!= null) holder.bind(dataItem)
    }
    object StoryComparator: DiffUtil.ItemCallback<StoryItem>() {
        override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
            return oldItem == newItem
        }
    }
}