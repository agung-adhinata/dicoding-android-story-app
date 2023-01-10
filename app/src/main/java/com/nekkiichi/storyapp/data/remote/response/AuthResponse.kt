package com.nekkiichi.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthResponse (
    @SerializedName("userId") var userId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("tokenId") var tokenId: String? = null
        ) : Parcelable

/**
 * full Data response from auth
 */
@Parcelize
data class FullAuthResponse (
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("loginResult") var loginResult: AuthResponse? = null
        ): Parcelable