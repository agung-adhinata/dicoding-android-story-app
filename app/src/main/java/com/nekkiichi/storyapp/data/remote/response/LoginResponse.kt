package com.nekkiichi.storyapp.data.remote.response

import android.os.Message
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse (
    @SerializedName("userId") var userId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("tokenId") var tokenId: String? = null
        ) : Parcelable

@Parcelize
data class FullLoginResponse (
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("loginResult") var loginResult: LoginResponse? = null
        ): Parcelable