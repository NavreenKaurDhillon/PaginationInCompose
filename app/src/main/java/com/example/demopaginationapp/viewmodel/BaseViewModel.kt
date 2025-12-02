package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.demopaginationapp.di.GoogleApi
import com.example.demopaginationapp.model.repositories.AppRepository
import com.example.demopaginationapp.model.repositories.MyPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BaseViewModel @Inject constructor(@GoogleApi private val appRepository: AppRepository) : ViewModel() {
    //here we setup the paging logic inside the viewmodel that interacts with the repo
    val pager = Pager(PagingConfig(pageSize = 10)) {
        //Pager - manages all the working of paging library

        //page size - tells the number of items that should be loaded in each network call

        MyPagingSource(appRepository = appRepository) //gets data and keeps keys(page numbers) of data
    }

    val pagingDataFlow = pager.flow
        .cachedIn(viewModelScope)  //scope of cached memory is tied to viewmodel's scope


}