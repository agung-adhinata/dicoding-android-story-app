package com.nekkiichi.storyapp.data.remote.services

import com.nekkiichi.storyapp.data.remote.request.Login
import com.nekkiichi.storyapp.data.remote.request.Register
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.data.remote.response.FullAuthResponse
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

interface ApiService {
    @POST("register")
    suspend fun sendRegister(
        @Body
        register: Register
    ):BasicResponse

    @POST("login")
    suspend fun sendLogin(
        @Body login: Login
    ): FullAuthResponse

    @Multipart
    @POST("stories")
    suspend fun sendStory(
        @Header("Authorization") tokenWithBeaver: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
    ): BasicResponse

    @Multipart
    @POST("stories")
    suspend fun sendStoryWithLocation(
        @Header("Authorization") tokenWithBeaver: String,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part photo: MultipartBody.Part,
    ): BasicResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") tokenWithBeaver: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") loc: Int
    ): ListStoryResponse
}