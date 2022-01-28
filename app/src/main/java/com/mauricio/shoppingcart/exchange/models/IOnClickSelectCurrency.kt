package com.mauricio.shoppingcart.exchange.models

import com.mauricio.shoppingcart.cart.models.CurrencyRate

interface IOnClickSelectCurrency {
    fun setCurrency(codeCurrency:CurrencyRate)
}

