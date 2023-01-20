package com.nekkiichi.storyapp.ui.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekkiichi.storyapp.data.AppPreferences
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: StoryRepository, private val preferences: AppPreferences):ViewModel() {
    private val _storyLists = MutableStateFlow<ResponseStatus<ListStoryResponse>>(ResponseStatus.Loading)
    val storyList = _storyLists.asStateFlow()
    val prevStoryList = MutableStateFlow<ListStoryResponse?>(null)
    fun getALlStories() {
        viewModelScope.launch {
            repository.getAllStories().collect {
                _storyLists.value = it
            }
        }
    }
    fun logOut() {
        viewModelScope.launch {
            preferences.clearSession()
            getALlStories()
        }
    }
}