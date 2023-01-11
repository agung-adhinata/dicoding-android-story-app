package com.nekkiichi.storyapp.data

import android.util.Log
import com.nekkiichi.storyapp.data.remote.request.Register
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.data.remote.services.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferences: AppPreferences
) {
    fun requestLogin(email: String, password: String):Flow<ResponseStatus<FullAuthResponse>> = flow {
        emit(ResponseStatus.loading)
        try {
            val res = apiService.sendLogin(email,password)
            emit(ResponseStatus.Success(res))
            if(res.error == false) {
                emit(ResponseStatus.Success(res))
            }else {
                Log.d(TAG, "Server Error, ${res.message}")
                emit(ResponseStatus.Error(res.message.toString()))
            }
        }catch (e:Exception) {
            Log.d(TAG, "error when request login, ${e.message}")
            emit(ResponseStatus.Error(e.message.toString()))
        }
    }
    fun requestRegister(name: String, email: String, password: String) : Flow<ResponseStatus<BasicResponse>> = flow {
        emit(ResponseStatus.loading)
        val req = Register(name, email, password)
        try {
            val res = apiService.sendRegister(req)
            if(res.error == false) {
                emit(ResponseStatus.Success(res))
            }else {
                Log.d(TAG, "Server Error, ${res.message}")
                emit(ResponseStatus.Error(res.message.toString()))
            }
        }catch (e: Exception) {
            Log.d(TAG, "Error when request register, ${e.message}")
            emit(ResponseStatus.Error(e.message.toString()))
        }
    }
    fun getAllStories(): Flow<ResponseStatus<ListStoryResponse>> = flow {
        val token = "Bearer ${preferences.getToken()}"
        emit(ResponseStatus.loading)
        try {
            val res = apiService.getAllStories(token, 10, 10)
            if (res.error == false) {
                emit(ResponseStatus.Success(res))
            } else {
                Log.d(TAG, "Server Error, ${res.message}")
                emit(ResponseStatus.Error(res.message.toString()))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error when request register, ${e.message}")
            emit(ResponseStatus.Error(e.message.toString()))
        }
    }

    companion object {
        val TAG = "StoryRepository"
    }
}