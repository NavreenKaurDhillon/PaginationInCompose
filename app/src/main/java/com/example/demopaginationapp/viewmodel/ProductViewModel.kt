package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.di.ProductApi
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(@ProductApi private val appRepository: AppRepository) : ViewModel() {

    private var rawProducts = MutableLiveData<Resource<ProductResponseData>>()
    var products: LiveData<Resource<ProductResponseData>> = rawProducts

  /*  rawProducts: LiveData<Resource<ProductResponseData>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(null, true))
        val response = appRepository.getProducts()
        emit(response)
    }*/


    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
//            emit(Resource.loading(null, true))
            val response = appRepository.getProducts()
            rawProducts.postValue(response)
//            emit(response)
        }
    }

    fun setSortOption(option: String) {
//        _sortOption.value = option //"None", "Price Low to High", "Price High to Low", "Rating High to Low"
        when(option){
            "Price Low to High" -> {
                val newProducts = rawProducts.value.data?.products?.sortedBy { it.price }
                rawProducts.postValue(Resource.success(newProducts?.let {
                    ProductResponseData(
                        products = it,
                        limit = products.value.data?.limit?.toInt(),
                        skip = products.value.data?.skip,
                        total = products.value.data?.total
                    )
                }))
            }
            "Price High to Low" -> {
                val newProducts = rawProducts.value.data?.products?.sortedByDescending { it.price }
                rawProducts.postValue(Resource.success(newProducts?.let {
                    ProductResponseData(
                        products = it,
                        limit = products.value?.data?.limit?.toInt(),
                        skip = products.value?.data?.skip,
                        total = products.value?.data?.total
                    )
                }))
            }
            "Rating High to Low" ->{
                val newProducts = rawProducts.value?.data?.products?.sortedBy { it.rating }
                rawProducts.postValue(Resource.success(newProducts?.let {
                    ProductResponseData(
                        products = it,
                        limit = products.value?.data?.limit?.toInt(),
                        skip = products.value?.data?.skip,
                        total = products.value?.data?.total
                    )
                }))
            }
        }
    }
}