package com.nekkiichi.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: ()-> Unit):LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRefresh.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            if(loadState is LoadState.Error) {
                binding.tvMessage.text = loadState.error.message
            }
            if(loadState is LoadState.Loading) {
                binding.tvMessage.text = binding.root.resources.getString(R.string.loading)
            }
            binding.tvMessage.isVisible = loadState is LoadState.Error || loadState is LoadState.Loading
            binding.btnRefresh.isVisible = loadState is LoadState.Error
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
        return LoadingStateViewHolder(binding,retry)
    }
}