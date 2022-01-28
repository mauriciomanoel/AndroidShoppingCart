package com.mauricio.shoppingcart.utils

import com.mauricio.shoppingcart.cart.models.CurrencyRate
import java.util.*

object Constant {
    const val SHOPPING = "ITEM_SHOPPING"
    val DEFAULT_CURRENCY_CODE = CurrencyRate("USD", Locale.US)
    const val DATABASE_NAME = "exchange_rate_database"
}