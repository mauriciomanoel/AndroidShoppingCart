package com.mauricio.shoppingcart.cart

typealias Currencies = ArrayList<Currency>

data class Currency (
    val code: String,
    val name: String,
    val locale: String?
)