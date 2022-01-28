package com.mauricio.shoppingcart.cart.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.cart.repository.CartRepository
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import com.mauricio.shoppingcart.utils.Constant.DEFAULT_CURRENCY_CODE
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@HiltViewModel
class CartViewModel @Inject constructor(val repository: CartRepository, val exchangeRepository: ExchangeRepository): ViewModel() {
//    @Inject constructor(val repository: BreedsRepository)

    val carts = MutableLiveData<ArrayList<Cart>>()
    val pairTotalCart = MutableLiveData<Pair<Currency?, Double>>()
    private var shoppingCarts = ArrayList<Cart>()
    val isFinishedLoadExchangeRate = MutableLiveData<Boolean>(false)
    private var codeCurrency: String = DEFAULT_CURRENCY_CODE
    val showLoading = MutableLiveData(false)
    val messageError = MutableLiveData<String>()


    //initializing the necessary components and classes


    override fun onCleared() {
        exchangeRepository.clear()
        super.onCleared()
    }

    fun setShoppingCart(json: String) {
        val listType = object : TypeToken<ArrayList<Cart>>() {}.type
        Gson().fromJson<ArrayList<Cart>>(json, listType)?.let {
            setShoppingCart(it)
        }
    }

    fun setShoppingCart(value: ArrayList<Cart>) {
        shoppingCarts = value
    }

    fun getExchangeRates() {
        showLoading()
        isFinishedLoadExchangeRate.value = false

//            hideLoading()
//            val currentTimestamp = System.currentTimeMillis().div(1000L)
//            // more 300 seconds
//            if (currentTimestamp.minus(it.timestampResponse!!) > 300) {
//                exchangeRepository.getExchangeRates(::processExchangeRates)
//            } else {
//                isFinishedLoadExchangeRate.value = true
//                calculateCurrencyPerCart(this.codeCurrency)
//            }
//        } ?: run {
            exchangeRepository.getExchangeRates(::processExchangeRates)
//        }
    }

    fun setExchangeRate(codeCurrency: String) {
        this.codeCurrency = codeCurrency
        getExchangeRates()
    }

    fun getCurrenciesToString(): String {
        val gson = Gson()
        return gson.toJson(exchangeRepository.getCurrencies())
    }

    fun getCurrencies() = exchangeRepository.getCurrencies()

    private fun calculateCurrencyPerCart(codeCurrency: String?) {
        var total = 0.0
        var defaultRate: Double? = null
        var toRate: Double? = null
        var currency = Currency.getInstance(codeCurrency);

//        var currency = exchangeRepository.getCurrencies().firstOrNull { it.code == codeCurrency }
        if (shoppingCarts.size == 0) return
//        exchangeRate?.let {
//            defaultRate = it.rates?.get(DEFAULT_CURRENCY_CODE)
//            toRate = it.rates?.get(codeCurrency)
////            currency = exchangeRepository.getCurrencies().firstOrNull { it.code == codeCurrency }
//        }


        shoppingCarts.forEach { cart ->
            if (defaultRate != null && toRate != null) {
                cart.totalAmountByCurrency = ExchangeUtils.currencyConverter(cart.totalAmount(), defaultRate!!, toRate!!)
            } else {
                cart.totalAmountByCurrency = cart.totalAmount()
            }
            cart.rateFormat = currency
            total+=cart.totalAmountByCurrency
        }
        carts.value = shoppingCarts
        pairTotalCart.value = Pair(currency, total)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        hideLoading()
        exchangeRate?.let {
//            this.exchangeRate = it
            isFinishedLoadExchangeRate.value = true
        }
        e?.let {
            messageError.value = e.message
            isFinishedLoadExchangeRate.value = false
        }
        calculateCurrencyPerCart(this.codeCurrency)
    }

    fun clear() {
        onCleared()
        hideLoading()
    }

    fun showLoading() {
        showLoading.postValue(true)
    }

    fun hideLoading() {
        showLoading.postValue(false)
    }
}