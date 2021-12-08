package com.mauricio.shoppingcart.exchange.models

data class ExchangeRate (
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    var timestampResponse: Long
)


