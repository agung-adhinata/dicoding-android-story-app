package com.nekkiichi.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.data.remote.services.ApiService
import retrofit2.HttpException
import java.io.IOException


class StoryListPagingSource (private val service: ApiService, private val preferences: AppPreferences): PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let {anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        val pageIndex = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val token = preferences.getTokenRaw() ?: throw ArithmeticException()
            val responseData = service.getAllStories("Bearer ${token}", pageIndex, 5,0)
            val listData: List<StoryItem> = responseData.listStory!!
            Log.d(TAG, "data fetched, ${listData[0]}")
            LoadResult.Page(
                data = listData,
                prevKey = if (pageIndex == INITIAL_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else pageIndex + 1
            )

        }catch (e: IOException) {
            Log.d(TAG, "Error Connection")
            return LoadResult.Error(e)
        }catch (e: HttpException) {
            Log.d(TAG, "Error Http Error")
            return LoadResult.Error(e)
        }catch (e: ArithmeticException) {
            Log.d(TAG, "Error Token")
            return LoadResult.Error(e)
        }catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
    companion object {
        val TAG = this::class.java.simpleName
        const val INITIAL_PAGE_INDEX = 1
    }
}