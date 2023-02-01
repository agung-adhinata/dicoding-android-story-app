package com.nekkiichi.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nekkiichi.storyapp.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: ()-> Unit, private val whenTokenInvalid: () -> Unit):LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    class LoadingStateViewHolder(private val binding: ItemLoadingBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {

        }
    }
    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate( LayoutInflater.from(parent.context),parent, false)
        return LoadingStateViewHolder(binding)
    }
}