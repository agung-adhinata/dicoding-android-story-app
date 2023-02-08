package com.nekkiichi.storyapp.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.nekkiichi.storyapp.data.remote.request.Login
import com.nekkiichi.storyapp.data.remote.request.Register
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.data.remote.services.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferences: AppPreferences
) {
    fun requestLogin(email: String, password: String): Flow<ResponseStatus<FullAuthResponse>> =
        flow {
            emit(ResponseStatus.Loading)
            try {
                val loginJson = Login(email, password)
                val res = apiService.sendLogin(loginJson)
                emit(ResponseStatus.Success(res))
                if (res.error == false) {
                    emit(ResponseStatus.Success(res))
                } else {
                    Log.d(TAG, "Server Error, ${res.message}")
                    emit(ResponseStatus.Error(res.message.toString()))
                }
            } catch (e: Exception) {
                Log.d(TAG, "error when request login, ${e.message}")
                emit(ResponseStatus.Error(e.message.toString()))
            }
        }

    fun requestRegister(
        name: String,
        email: String,
        password: String
    ): Flow<ResponseStatus<BasicResponse>> = flow {
        emit(ResponseStatus.Loading)
        val req = Register(name, email, password)
        try {
            val res = apiService.sendRegister(req)
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

    fun getAllStoriesWithLocation(page: Int = 1, size: Int = 20):Flow<ResponseStatus<ListStoryResponse>> = flow {
        preferences.getToken().collect {
            if (it.isNullOrEmpty()) {
                Log.d(TAG, "Token Invalid")
                emit(ResponseStatus.TokenInvalid)
            } else {
                val token = "Bearer $it"
                emit(ResponseStatus.Loading)
                try {
                    val res = apiService.getAllStories(token, page, size, 1)
                    if (res.error == false) {
                        Log.d(TAG, "data: ${res.listStory.toString()}")
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
        }
    }

    fun getAllStories(page: Int = 1, size: Int = 5): Flow<ResponseStatus<ListStoryResponse>> =
        flow {
            preferences.getToken().collect {
                if (it.isNullOrEmpty()) {
                    Log.d(TAG, "Token Invalid")
                    emit(ResponseStatus.TokenInvalid)
                } else {
                    val token = "Bearer $it"
                    emit(ResponseStatus.Loading)
                    try {
                        val res = apiService.getAllStories(token, page, size, 0)
                        if (res.error == false) {
                            Log.d(TAG, "data: ${res.listStory.toString()}")
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
            }
        }
    fun getAllStoriesPager(): Flow<PagingData<StoryItem>> {
        Log.d(TAG,"getting data from pager")
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryListPagingSource(apiService, preferences)
            }
        ).flow
    }

    fun uploadStory(file: File, description: String): Flow<ResponseStatus<BasicResponse>> = flow {
        preferences.getToken().collect {
            if (it.isNullOrEmpty()) {
                Log.d(TAG, "Token Invalid")
                emit(ResponseStatus.TokenInvalid)
            } else {
                val token = "Bearer $it"
                emit(ResponseStatus.Loading)
                try {
                    val requestMediaFile = file.asRequestBody("image/jpeg".toMediaType())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestMediaFile
                    )
                    val desc = description.toRequestBody("text/plain".toMediaType())

                    val res = apiService.sendStory(token, desc, imageMultipart)

                    //err check
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
        }
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}