package com.nekkiichi.storyapp.ui.view.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekkiichi.storyapp.data.AppPreferences
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: StoryRepository,
    private val preferences: AppPreferences
) : ViewModel() {
    private val _loginStatus = MutableStateFlow<ResponseStatus<FullAuthResponse>>(ResponseStatus.Init)
    val loginStatus = _loginStatus.asStateFlow()
    private val _registerResponse =
        MutableStateFlow<ResponseStatus<BasicResponse>>(ResponseStatus.Init)
    val registerResponse = _registerResponse.asStateFlow()
    private val _tokenStatus = MutableStateFlow<ResponseStatus<ListStoryResponse>>(ResponseStatus.Loading)
    val tokenStatus = _tokenStatus.asStateFlow()

    init {
        checkTokenValidity()
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            repository.requestLogin(email, password).collect {
                _loginStatus.value = it
                when (it) {
                    is ResponseStatus.Success -> {
                        val data = it.data.loginResult
                        Log.d(TAG, "data received: ${data}")
                        if (data != null) {
                            updateSession(data.token)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
    fun createNewAccount(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.requestRegister(name, email, password).collect {
                _registerResponse.value = it
            }
        }
    }
    private fun updateSession(token: String) {
        viewModelScope.launch {
            preferences.saveSession(token)
        }
    }
    fun checkTokenValidity() {
        viewModelScope.launch {
            repository.getAllStories(0,0).collect {
                _tokenStatus.value = it
            }
        }
    }
    companion object {
        val TAG = this::class.java.simpleName
    }
}