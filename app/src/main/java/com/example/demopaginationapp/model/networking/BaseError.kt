package com.example.demopaginationapp.model.networking

class BaseError {
    //error handling class - used to interpret error to viewmodel whether it is local or network based error
    var code = 0
    var message = ""

    constructor()
    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }
}