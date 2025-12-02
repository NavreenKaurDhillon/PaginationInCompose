package com.example.demopaginationapp.model.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.demopaginationapp.di.GoogleBaseUrl
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import javax.inject.Inject
import kotlin.math.max


private const val STARTING_KEY = 1


//tells how to fetch the data - whether from API or database
//MyPagingSource acts as an intermediate between ui and repo
class MyPagingSource @Inject constructor(@GoogleBaseUrl private val appRepository: AppRepository) : PagingSource<Int, ResponseDataItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseDataItem>): Int? {
        // calculates fresh key
        val anchorPosition = state.anchorPosition ?: return null //anchorPosition-pos near to middle - > used to keep the user's current focus item in list instead of fully refreshing whole page
        val item = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = item.id - (state.config.pageSize / 2)) //return middle key (10 - 50/2) -> -15 ->1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseDataItem> {
        val startKey = params.key ?: STARTING_KEY
        return try {
          //call api to fetch here
            val response = appRepository.getList(
                page = startKey, // Pass the key as the page index
                perPage = 20 // Pass the size as the page size
            )

            LoadResult.Page(
                data = response.data as List<ResponseDataItem>,
                prevKey = if (startKey == STARTING_KEY) null else ensureValidKey(key = startKey - 1),
                nextKey = if (response.data.isEmpty()) null else startKey + 1

            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}