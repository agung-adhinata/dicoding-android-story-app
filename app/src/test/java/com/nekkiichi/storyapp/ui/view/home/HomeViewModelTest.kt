package com.nekkiichi.storyapp.ui.view.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.nekkiichi.storyapp.adapter.StoryListAdapter
import com.nekkiichi.storyapp.data.AppPreferences
import com.nekkiichi.storyapp.data.StoryRepository
import com.nekkiichi.storyapp.data.remote.response.StoryItem
import com.nekkiichi.storyapp.utils.DataDummy
import com.nekkiichi.storyapp.utils.MainDispacherRule
import com.nekkiichi.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchRule = MainDispacherRule()

    @Mock private lateinit var storyRepository: StoryRepository
    @Mock private lateinit var preferences: AppPreferences

    private val dummyStoriesResponse = DataDummy.generateDummyStoryList().listStory!!

    @Test
    fun `stories shouldn't null and return success`() = runTest {
        val data = StoryListPagingSource.snapshot(dummyStoriesResponse)
        val expectedData = MutableLiveData<PagingData<StoryItem>>()
        expectedData.value = data
        Mockito.`when`(storyRepository.getAllStoriesPager()).thenReturn(expectedData)

        val homeViewModel = HomeViewModel(storyRepository,preferences)

        val actualStories = homeViewModel.stories.getOrAwaitValue()

        val diffs = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.StoryComparator,
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        diffs.submitData(actualStories)

        Assert.assertNotNull(diffs.snapshot())
        Assert.assertEquals(dummyStoriesResponse, diffs.snapshot())
        Assert.assertEquals(dummyStoriesResponse.size, diffs.snapshot().size)
        Assert.assertEquals(dummyStoriesResponse[0].id, diffs.snapshot()[0]?.id)
    }
}

val listUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryListPagingSource: PagingSource<Int, StoryItem>() {
    companion object {
        fun snapshot(item: List<StoryItem>):PagingData<StoryItem> {
            return PagingData.from(item)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return LoadResult.Page(emptyList(), 0,1)
    }

}