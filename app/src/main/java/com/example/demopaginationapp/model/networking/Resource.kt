package com.example.demopaginationapp.model.networking

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    //used to communicate between data an ui
    companion object {
        fun <T> success(data: T?): Resource<T> {
//            Progress.hide()
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
//            Progress.hide()
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?, showLoader : Boolean = true): Resource<T> {
            CoroutineScope(Dispatchers.Main).launch{
                if (showLoader) {
//                    Progress.show(activity)
                }
            }
            return Resource(Status.LOADING, data, null)

        }
    }
}
enum class Status {
    SUCCESS, ERROR, LOADING ,NO_INTERNET
}