package com.mauricio.shoppingcart.network

data class ErrorResult (
    val error: ErrorResponse
)

data class ErrorResponse (
    val code: String,
    val message: String
)
