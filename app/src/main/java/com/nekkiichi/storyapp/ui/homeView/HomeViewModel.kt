package com.nekkiichi.storyapp.ui.homeView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekkiichi.storyapp.data.AppPreferences
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
class HomeViewModel @Inject constructor(private val repository: StoryRepository):ViewModel() {
    private val _storyLists = MutableStateFlow<ResponseStatus<ListStoryResponse>>(ResponseStatus.loading)
    val storyList = _storyLists.asStateFlow()
    init {
        getALlStories()
    }
    fun getALlStories() {
        viewModelScope.launch {
            repository.getAllStories().collect {
                _storyLists.value = it
            }
        }
    }
}