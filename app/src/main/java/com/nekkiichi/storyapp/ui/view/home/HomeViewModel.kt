package com.nekkiichi.storyapp.ui.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nekkiichi.storyapp.data.AppPreferences
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: StoryRepository, private val preferences: AppPreferences):ViewModel() {
    val stories : LiveData<PagingData<StoryItem>> = repository.getAllStoriesPager().cachedIn(viewModelScope)
    fun logOut() {
        viewModelScope.launch {
            preferences.clearSession()
        }
    }

}