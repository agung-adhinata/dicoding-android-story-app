package com.nekkiichi.storyapp.utils

import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.data.remote.response.StoryItem

object DataDummy {
    fun generateDummyStoryList(): ListStoryResponse {
        val storyList = ArrayList<StoryItem>()
        for (i in 1..10) {
            val story = StoryItem(
                id = "2391",
                name = "stori $i",
                description = "simple story ${i*2}",
                createdAt = "2022-02-22T22:22:22Z",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png"
            )
            storyList.add(story)
        }
        return ListStoryResponse(
            error = false,
            message = null,
            listStory = storyList
        )
    }
    fun generateEmptyStoryList(): ListStoryResponse {
        return ListStoryResponse(
            error = false,
            message = null,
            listStory = listOf()
        )
    }
}