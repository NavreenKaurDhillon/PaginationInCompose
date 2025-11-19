package com.example.demopaginationapp.model.networking

import com.example.demopaginationapp.common.Constants
import com.example.demopaginationapp.model.dataclasses.ResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET(Constants.LISTING)
    suspend fun getList(
        @Query("page") page : Int,
        @Query("per_page") perPage : Int,
    ): ResponseData

}