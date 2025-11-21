package com.example.demopaginationapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProductBaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleBaseUrl


// Qualifier for the RetrofitInterface instance and the AppRepository instance (Google API scope)
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleApi

// Qualifier for the RetrofitInterface instance and the AppRepository instance (Product API scope)
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProductApi