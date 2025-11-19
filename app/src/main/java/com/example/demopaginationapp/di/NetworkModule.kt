package com.example.demopaginationapp.di

import com.example.demopaginationapp.common.Constants.BASE_URL
import com.example.demopaginationapp.model.networking.ResponseHandler
import com.example.demopaginationapp.model.networking.RetrofitInterface
import com.example.demopaginationapp.model.repositories.AppRepository
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

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request: Request
            /*   if (!getFromDatabase(Constants.AUTH_TOKEN,"").isNullOrEmpty()) {
                   Log.d("tokennn", "provideLoggingInterceptor: ${getFromDatabase(Constants.AUTH_TOKEN,"")}")
                   val auth :String = getFromDatabase(Constants.AUTH_TOKEN,"")
                   Log.e("Authorization", "" + getFromDatabase(Constants.AUTH_TOKEN,""))
                   request = chain.request().newBuilder().header("Authorization", "Bearer $auth")
                       .addHeader("Content-Type", "application/json")
                       .addHeader("Accept", "application/json")
                       .addHeader("secretkey","U2FsdGVkX18cWH+rfiUQiJ0Vn6M8vIfT/2pup77qAzU2mkMfLGJqtGOjoGlfgGRn")
                       .addHeader("publishkey","U2FsdGVkX1+iE5oRiAoYXkDa3fkrK3pjpPn27JNc1tkcoPAE+rk3OMJesdQp03bE")
                       .build()
               } else {*/
            request = chain.request().newBuilder()
//                    .addHeader("Accept", "application/json")
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("secretkey","U2FsdGVkX18cWH+rfiUQiJ0Vn6M8vIfT/2pup77qAzU2mkMfLGJqtGOjoGlfgGRn")
//                    .addHeader("publishkey","U2FsdGVkX1+iE5oRiAoYXkDa3fkrK3pjpPn27JNc1tkcoPAE+rk3OMJesdQp03bE")
                .build()
//        }
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

    @Singleton
    @Provides
    fun provideRepository(apiService: RetrofitInterface, responseHandler: ResponseHandler): AppRepository {
        return AppRepository(apiService, responseHandler)
    }


}