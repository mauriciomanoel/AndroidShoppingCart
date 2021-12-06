package com.mauricio.shoppingcart.cart.models

data class Cart (
    var item: Long,
    var description: String,
    var price: Double,
    var totalItem: Int,
    var totalAmountByCurrency: Double=0.0) {
    var rateFormat: Currency? = null

    fun totalAmount(): Double {
        return price.times(totalItem)
    }
}