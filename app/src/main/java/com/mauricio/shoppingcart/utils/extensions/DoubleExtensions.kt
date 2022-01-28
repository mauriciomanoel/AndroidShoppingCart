package com.mauricio.shoppingcart.utils.extensions

import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.utils.Constant
import java.text.NumberFormat
import java.util.*

fun Double.formatNumber(currencyRate: CurrencyRate? = null): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2

    currencyRate?.let {
        format.currency = Currency.getInstance(it.code)
    } ?: run {
        format.currency = Currency.getInstance(Constant.DEFAULT_CURRENCY_CODE.code)
    }
    return "${format.format(this)}"
}
