package com.example.demopaginationapp.model.repositories

import com.example.demopaginationapp.model.dataclasses.CategoriesResponseData
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.dataclasses.ResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.ResponseHandler
import com.example.demopaginationapp.model.networking.RetrofitInterface
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: RetrofitInterface,
    private var responseHandler: ResponseHandler
) {

    //get list
    suspend fun getList(
        page: Int, perPage: Int
    ): Resource<ResponseData> {
        return try {
            responseHandler.handleResponse(apiService.getList(page, perPage))
        } catch (e: Exception) {

            responseHandler.handleException(e)
        }
    }

    suspend fun getProducts(
    ): Resource<ProductResponseData> {
        return try {
            responseHandler.handleResponse(apiService.getProducts())
        } catch (e: Exception) {

            responseHandler.handleException(e)
        }
    }

    suspend fun getCategories(parentId: Int): Resource<CategoriesResponseData> {
        return try {
            responseHandler.handleResponse(
                apiService.getCategories(
                   parentId.toString()
                )
            )
        } catch (e: Exception) {

            responseHandler.handleException(e)
        }
    }
}