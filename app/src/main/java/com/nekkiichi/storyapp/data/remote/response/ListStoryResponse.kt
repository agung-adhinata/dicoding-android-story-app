package com.nekkiichi.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListStoryResponse (
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("listStory") var listStory: ListStoryItemResponse? = null
        ):Parcelable
@Parcelize
data class ListStoryItemResponse (
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("photoUrl") var photoUrl: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
        ):Parcelable