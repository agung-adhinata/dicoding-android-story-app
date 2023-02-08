package com.nekkiichi.storyapp.data

import com.nekkiichi.storyapp.data.remote.request.Login
import com.nekkiichi.storyapp.data.remote.request.Register
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.data.remote.services.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {
    override suspend fun sendRegister(register: Register): BasicResponse {
        TODO("Not yet implemented")
    }

    override suspend fun sendLogin(login: Login): FullAuthResponse {
        TODO("Not yet implemented")
    }

    override suspend fun sendStory(
        tokenWithBeaver: String,
        description: RequestBody,
        photo: MultipartBody.Part
    ): BasicResponse {
        TODO("Not yet implemented")
    }

    override suspend fun sendStoryWithLocation(
        tokenWithBeaver: String,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        photo: MultipartBody.Part
    ): BasicResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStories(
        tokenWithBeaver: String,
        page: Int,
        size: Int,
        loc: Int
    ): ListStoryResponse {
        TODO("Not yet implemented")
    }
}