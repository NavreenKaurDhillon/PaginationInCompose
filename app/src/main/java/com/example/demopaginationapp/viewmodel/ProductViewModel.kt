package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.demopaginationapp.di.ProductApi
import com.example.demopaginationapp.di.ProductBaseUrl
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(@ProductApi private val appRepository: AppRepository) : ViewModel() {
    val products: LiveData<Resource<ProductResponseData>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(null, true))
        val response = appRepository.getProducts()
        emit(response)
    }
  /*  fun getProducts()
            : LiveData<Resource<ProductResponseData>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(null, true))
            val response = appRepository.getProducts()
            emit(response)
        }
    }*/

}