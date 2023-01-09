package com.nekkiichi.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasicResponse (
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null
        ): Parcelable