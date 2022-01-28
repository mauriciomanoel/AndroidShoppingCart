package com.mauricio.shoppingcart.utils.exchange

import android.util.Log

object ExchangeUtils {
    @JvmStatic
    fun currencyConverter(value: Double?, rateDefault: Double, rateTo: Double): Double {
//        Log.v("TAG: ExchangeUtils", "value=${value}")
        value?.let {  amount->
            // Converter current dollar to euro
            val valueDollarToEuro = amount.div(rateDefault)
            // Converter euro to new currency
            val valueConverted = valueDollarToEuro.times(rateTo)
//            Log.v("TAG: ExchangeUtils", "valueConverted=${valueConverted}")
            return valueConverted
        }
        return 0.0
    }
}