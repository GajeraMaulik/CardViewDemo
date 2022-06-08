package com.example.cardviewdemo.Paging

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.cardviewdemo.databinding.ActivityPagingBinding
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.Client
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.coroutines.flow.Flow

class PagingActivityViewModel:ViewModel() {

     var apiServices: APIServices
        lateinit var binding: ActivityPagingBinding
    init {
        apiServices = Client.getPaging().create(APIServices::class.java)
    }

    fun getListData(): Flow<PagingData<CharaterData>>{

        return Pager(config = PagingConfig(pageSize = 42, maxSize = 200),
            pagingSourceFactory = { CharacterPagingSouce(apiServices) }).flow.cachedIn(viewModelScope)
    }
}