package com.mauricio.shoppingcart.utils.number

import java.text.NumberFormat
import java.util.*

object NumberUtils {

    @JvmStatic
    fun formatNumber(number: Double): String {
        val nf = NumberFormat.getInstance(Locale.US)
        nf.minimumFractionDigits = 2
        return "${nf.currency.currencyCode} ${nf.format(number)}"
    }

    @JvmStatic
    fun formatNumber(value: Double?, tagLocale: String?): String {
        value?.let {  amount->
            var locale = Locale("en", "US")
            if (!tagLocale.isNullOrEmpty()) {
                val tagSplit = tagLocale.split("_")
                when(tagSplit.size) {
                    1 -> locale = Locale(tagSplit[0])
                    2 -> locale = Locale(tagSplit[0], tagSplit[1])
                }
            }
            // Formatting Currency
            val nf = NumberFormat.getInstance(locale)
            nf.minimumFractionDigits = 2
            nf.maximumFractionDigits = 2
            return "${nf.currency.currencyCode} ${nf.format(amount)}"
        }
        return "-"
    }
}