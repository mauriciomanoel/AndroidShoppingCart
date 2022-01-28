package com.mauricio.shoppingcart.exchange.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.cart.models.Currency
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class ExchangeViewModel@Inject constructor(private val repository: ExchangeRepository): ViewModel() {

    val messageError = MutableLiveData<String>()


    fun loadCurrencies(value: String?): ArrayList<Currency> {
        val listType = object : TypeToken<ArrayList<Currency>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun getExchangeRates() {
        repository.getExchangeRates(::processExchangeRates)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        exchangeRate?.let {
//            AndroidShoppingCartApplication.exchangeRate = it
        }
        e?.let {
            messageError.value = it.message
        }
    }

}