package com.nekkiichi.storyapp.ui.authActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: StoryRepository):ViewModel() {
    private val _session = MutableStateFlow<ResponseStatus<FullAuthResponse>>(ResponseStatus.init)
    val session = _session.asStateFlow()
    private val _registerResponse = MutableStateFlow<ResponseStatus<BasicResponse>>(ResponseStatus.init)
    val registerResponse = _registerResponse.asStateFlow()
    fun logIn(email: String, password: String) {
        viewModelScope.launch {

            repository.requestLogin(email,password).collect{
                _session.value = it
            }
        }
    }
    fun createNewAccount(name:String, email: String, password: String) {
        viewModelScope.launch {
            repository.requestRegister(name,email,password).collect{
                _registerResponse.value = it
            }
        }
    }

}