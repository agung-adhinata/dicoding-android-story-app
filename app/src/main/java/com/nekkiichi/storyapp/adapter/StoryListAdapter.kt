package com.nekkiichi.storyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.ListStoryItemResponse
import com.nekkiichi.storyapp.databinding.ItemStorylistBinding
import javax.inject.Inject

class StoryListAdapter(private val listData: List<ListStoryItemResponse>, private val context: Context):RecyclerView.Adapter<StoryListAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: ItemStorylistBinding): RecyclerView.ViewHolder(itemView.root) {
        lateinit var storyId: String
        var ivContent = itemView.ivContent
        var tvUsername = itemView.tvOwnername
        var tvDescription = itemView.tvContentDescription
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStorylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return  listData.size
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataItem = listData[position]
        holder.storyId = dataItem.id
        holder.tvUsername.text = dataItem.name
        holder.tvDescription.text = dataItem.description
        Glide.with(holder.itemView.context).load(dataItem.photoUrl).into(holder.ivContent)
    }
}