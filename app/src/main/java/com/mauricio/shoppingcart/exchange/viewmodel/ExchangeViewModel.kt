package com.mauricio.shoppingcart.exchange.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.cart.models.Currency
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@HiltViewModel
class ExchangeViewModel@Inject constructor(private val repository: ExchangeRepository): ViewModel() {

    val messageError = MutableLiveData<String>()

    private val _listCurrencies = MutableLiveData<List<CurrencyRate>>()
    val listCurrencies: LiveData<List<CurrencyRate>> = _listCurrencies

    fun getListCurrencyRate() {
        repository.getListCurrencyRate {
            _listCurrencies.postValue(it)
        }
    }

}