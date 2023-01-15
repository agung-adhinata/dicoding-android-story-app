package com.nekkiichi.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListStoryResponse (
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("listStory") var listStory: List<StoryItem>? = null
        ):Parcelable
@Parcelize
data class StoryItem (
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("photoUrl") var photoUrl: String,
    @SerializedName("createdAt") var createdAt: String,
        ):Parcelable