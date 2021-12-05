package com.mauricio.shoppingcart.cart

data class Cart (
    var item: Long,
    var description: String,
    var price: Double,
    var totalItem: Int) {

    fun totalAmount(): Double {
        return price.times(totalItem)
    }
}