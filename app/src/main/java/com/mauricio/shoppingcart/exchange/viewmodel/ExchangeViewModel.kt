package com.mauricio.shoppingcart.exchange.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.cart.models.Currency
import com.mauricio.shoppingcart.di.component.DaggerAppComponent
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import javax.inject.Inject

class ExchangeViewModel@Inject constructor(private val application: Application): ViewModel() {

    @Inject
    lateinit var repository: ExchangeRepository

    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(application).build().inject(this)
    }

    fun loadCurrencies(value: String?): ArrayList<Currency> {
        val listType = object : TypeToken<ArrayList<Currency>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun getExchangeRates() {
        repository.getExchangeRates(BuildConfig.API_KEY, ::processExchangeRates)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        exchangeRate?.let {
            AndroidShoppingCartApplication.exchangeRate = it
        }
        e?.let {}
    }

}