package com.mauricio.shoppingcart.utils.number

import java.text.NumberFormat
import java.util.*

object NumberUtils {

    @JvmStatic
    fun formatNumber(number: Double): String {
        val nf = NumberFormat.getInstance(Locale.US)
        nf.minimumFractionDigits = 2
        return "${nf.format(number)}${nf.currency.symbol}"
    }
}