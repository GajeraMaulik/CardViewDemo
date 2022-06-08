package com.example.cardviewdemo.Paging

import android.net.Uri
import android.util.Log.d
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cardviewdemo.services.APIServices
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CharacterPagingSouce(val apiServices: APIServices):PagingSource<Int, CharaterData>() {
    override fun getRefreshKey(state: PagingState<Int, CharaterData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharaterData> {
        return try {
            val nextpage = params.key ?: FIRST_PAGE_INDEX
            d("101010","101010---->$nextpage")
            val response = apiServices.getDataFromApi(nextpage)
            d("201010","202020 ---->$response")
            var nextPageNumber:Int? = null
            d("301010","303030  ---->$nextPageNumber")

            if(response.info.next != null){
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                d("401010","404040   ---->$nextPageQuery")
                nextPageNumber = nextPageQuery?.toInt()
            }
            LoadResult.Page(data=response.results,prevKey = null, nextKey = nextPageNumber)
        }
        catch (e:Exception){
            LoadResult.Error(e)
        }
    }
    companion object{
        private const val FIRST_PAGE_INDEX = 1
    }
}