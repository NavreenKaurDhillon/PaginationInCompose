package com.example.demopaginationapp.model.networking

import com.example.demopaginationapp.model.dataclasses.CategoriesResponseData
import com.example.demopaginationapp.utils.Constants
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.dataclasses.ResponseData
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {

    //pagination listing api
    @GET(Constants.LISTING)
    suspend fun getList(
        @Query("page") page : Int,
        @Query("per_page") perPage : Int,
    ): ResponseData


    //dummy products list api
    @GET(Constants.PRODUCTS_LIST)
    suspend fun getProducts(
    ): ProductResponseData

    //categories list api
    @Headers(
//        "X-TOKEN:612ccd3ef3b5b87b7195348c19efafb9" ,
            "Content-Type: application/x-www-form-urlencoded" ,
            "X-LANGUAGE-ID:1" ,
            "X-CURRENCY-ID:1" ,
//            "X-YK-LAT:" ,
//            "X-YK-LNG:" ,
            "X-YK-STATE-CODE:PB" ,
            "X-YK-COUNTRY-CODE:IN")
    @FormUrlEncoded
    @POST(Constants.CATEGORIES_LIST)
    suspend fun getCategories(@Field ("parentId") id: String): CategoriesResponseData

}