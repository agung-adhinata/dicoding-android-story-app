package com.nekkiichi.storyapp.ui.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekkiichi.storyapp.data.AppPreferences
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val repository: StoryRepository, private val preferences: AppPreferences):ViewModel() {
    private val _isLogin = MutableStateFlow<ResponseStatus<ListStoryResponse>>(ResponseStatus.loading)
    val isLogin = _isLogin.asStateFlow()
    init {
        checkTokenValidity()
    }
    fun checkTokenValidity() {
        viewModelScope.launch {
            repository.getAllStories(0,0).collect {
                _isLogin.value = it
            }
        }
    }
}