package com.mauricio.shoppingcart.cart

typealias Currencies = HashMap<String, Currency>

data class Currency (
    val name: String,
    val locale: String?
)