package com.mauricio.shoppingcart.utils.exchange

import android.util.Log
import java.text.NumberFormat
import java.util.*

object ExchangeUtils {
    @JvmStatic
    fun currencyConverter(value: Double?, defaultCurrency: String = "USD", toCurrency:String, rateDefaultCurrent: Double, rateToCurrent: Double): String {
        Log.v("TAG: ExchangeUtils", "value=${value}")

        value?.let {  amount->
            // Converter current dollar to euro
            val valueDollarToEuro = amount.div(rateDefaultCurrent)
            // Converter euro to new currency
            val valueConverted = valueDollarToEuro.times(rateToCurrent)
            Log.v("TAG: ExchangeUtils", "valueConverted=${valueConverted}")
            // Formatting Currency _
            val nf = NumberFormat.getInstance(Locale("gv", "GB"))
            nf.minimumFractionDigits = 2
            nf.maximumFractionDigits = 2
            return "${toCurrency} ${nf.format(valueConverted)}"
        }
        return "-"
    }
}