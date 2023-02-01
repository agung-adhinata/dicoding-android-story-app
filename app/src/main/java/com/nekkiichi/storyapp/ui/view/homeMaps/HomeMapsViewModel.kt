package com.nekkiichi.storyapp.ui.view.homeMaps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMapsViewModel @Inject constructor(private val repository: StoryRepository):ViewModel() {
    private val _stories = MutableStateFlow<ResponseStatus<ListStoryResponse>>(ResponseStatus.Loading)
    val stories = _stories.asStateFlow()
    init {
        getStories()
    }

    fun getStories() {
        viewModelScope.launch {
            repository.getAllStoriesWithLocation().collect {
                _stories.value = it
            }
        }
    }
}