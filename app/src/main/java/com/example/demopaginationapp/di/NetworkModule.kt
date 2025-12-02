package com.example.demopaginationapp.di

import com.example.demopaginationapp.model.networking.ResponseHandler
import com.example.demopaginationapp.model.networking.RetrofitInterface
import com.example.demopaginationapp.model.repositories.AppRepository
import com.example.demopaginationapp.utils.Constants.BASE_URL
import com.example.demopaginationapp.utils.Constants.CATEGORIES_BASE_URL
import com.example.demopaginationapp.utils.Constants.PRODUCTS_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    var isHeadersRequired = false

    @GoogleBaseUrl
    @Provides
    fun provideGoogleBaseUrl(): String {
        return BASE_URL  //used to get repos list
    }

    @ProductBaseUrl
    @Provides
    fun provideProductBaseUrl(): String {
        return PRODUCTS_BASE_URL   //used to get products list
    }

    @CategoriesBaseUrl
    @Provides
    fun provideCategoryBaseUrl(): String {
        return CATEGORIES_BASE_URL   //used to get products list
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request: Request
            if (isHeadersRequired) {
                request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()
            } else {
                request = chain.request().newBuilder()
                    .build()
            }
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(httpLoggingInterceptor: Interceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30 * 3, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        return retrofit.build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): RetrofitInterface {
        return retrofit.create(RetrofitInterface::class.java)
    }

    @GoogleApi // Qualifies the returned RetrofitInterface instance as the Google API
    @Singleton
    @Provides
    fun provideGoogleApiService(
        @GoogleBaseUrl baseUrl: String, // Inject the qualified Google URL
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): RetrofitInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(RetrofitInterface::class.java)
    }

    @CategoriesApi // Qualifies the returned RetrofitInterface instance as CategoriesApi
    @Singleton
    @Provides
    fun provideCategoryApiService(
        @CategoriesBaseUrl baseUrl: String, // Inject the qualified Google URL
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): RetrofitInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(RetrofitInterface::class.java)
    }

    @ProductApi // Qualifies the returned RetrofitInterface instance as the Product API
    @Singleton
    @Provides
    fun provideProductApiService(
        @ProductBaseUrl baseUrl: String, // Inject the qualified Product URL
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): RetrofitInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(RetrofitInterface::class.java)
    }

    /**
     * QUALIFIED REPOSITORY PROVIDERS
     * Qualifier on the return type is CRITICAL to distinguish between the two AppRepository instances.
     */

    @Singleton
    @Provides
    @GoogleApi
    fun provideGoogleRepository(
        @GoogleApi apiService: RetrofitInterface, // Inject the qualified Google API service
        responseHandler: ResponseHandler
    ): AppRepository {
        return AppRepository(apiService, responseHandler)
    }

    @Singleton
    @Provides
    @ProductApi
    fun provideProductRepository(
        @ProductApi apiService: RetrofitInterface, // Inject the qualified Product API service
        responseHandler: ResponseHandler
    ): AppRepository {
        return AppRepository(apiService, responseHandler)
    }

    @Singleton
    @Provides
    @CategoriesApi
    fun provideCategoryRepository(
        @CategoriesApi apiService: RetrofitInterface, // Inject the qualified Product API service
        responseHandler: ResponseHandler
    ): AppRepository {
        return AppRepository(apiService, responseHandler)
    }

}