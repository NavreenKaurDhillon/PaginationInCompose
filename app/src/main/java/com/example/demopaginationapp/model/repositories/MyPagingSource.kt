package com.example.demopaginationapp.model.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import javax.inject.Inject
import kotlin.math.max


// The initial key used for loading.
// This is the item id of the first item that will be loaded
private const val STARTING_KEY = 1
private const val LOAD_DELAY_MILLIS = 3_000L  //delay after each page increment


//tells how to fetch the data - whether from API or database
//MyPagingSource acts as an intermediate between ui and repo
class MyPagingSource @Inject constructor(private val appRepository: AppRepository) : PagingSource<Int, ResponseDataItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseDataItem>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null //anchorPosition-pos near to middle
        val item = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = item.id - (state.config.pageSize / 2))
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseDataItem> {
        val startKey = params.key ?: STARTING_KEY

        Log.d("kfhkjefhje", "load: ${params.key}   ${params.loadSize} ")
        return try {
            // --- ACTUAL API CALL HERE ---
            val response = appRepository.getList(
                page = startKey, // Pass the key as the page index
                perPage = 20 // Pass the size as the page size
            )
            // -----------------------------

            // Assuming the API returns a List<ResponseDataItem>
            LoadResult.Page(
                data = response.data as List<ResponseDataItem>,
                prevKey = if (startKey == STARTING_KEY) null else ensureValidKey(key = startKey - 1),
                nextKey = if (response.data.isEmpty()) null else startKey + 1
                // Note: The nextKey logic might need adjustment based on how your API defines 'page'
                // Here we assume the key increments by loadSize (offset-based)
            )
        } catch (e: Exception) {
            // Handle errors like IOException (network issues) or HttpException (API errors)
            LoadResult.Error(e)
        }
    }


    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}