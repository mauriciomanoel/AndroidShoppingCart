package com.mauricio.shoppingcart.exchange

data class ExchangeRate (
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)


