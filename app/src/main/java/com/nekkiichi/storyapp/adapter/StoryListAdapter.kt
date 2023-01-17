package com.nekkiichi.storyapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.databinding.ItemStorylistBinding
import com.nekkiichi.storyapp.ui.view.home.DetailActivity

class StoryListAdapter(private val listData: List<StoryItem>, private val context: Context) :
    RecyclerView.Adapter<StoryListAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: ItemStorylistBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        lateinit var storyId: String
        var ivContent = itemView.ivItemPhoto
        var tvUsername = itemView.tvItemName
        var tvDescription = itemView.tvContentDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemStorylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataItem = listData[position]
        holder.storyId = dataItem.id
        holder.tvUsername.text = dataItem.name
        holder.tvDescription.text = dataItem.description
        Glide.with(holder.itemView.context).load(dataItem.photoUrl).thumbnail().apply(
            RequestOptions.bitmapTransform(
                RoundedCorners(
                    context.resources.getDimensionPixelSize(
                        R.dimen.roundedIvSize
                    )
                )
            )
        ).into(holder.ivContent)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, dataItem)
            holder.itemView.context.startActivity(intent)
        }
    }
}