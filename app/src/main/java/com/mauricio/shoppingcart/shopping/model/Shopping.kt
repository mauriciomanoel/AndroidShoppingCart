package com.mauricio.shoppingcart.shopping.model

data class Shopping (
    var item: Long,
    var description: String,
    var price: Double,
    var amount: Int) {

    fun totalAmount(): Double {
        return price.times(amount)
    }
}