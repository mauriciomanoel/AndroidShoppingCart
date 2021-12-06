package com.mauricio.shoppingcart.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.di.component.DaggerAppComponent
import com.mauricio.shoppingcart.dorms.model.Dorm
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils
import com.mauricio.shoppingcart.utils.file.FileUtils
import javax.inject.Inject

class CartViewModel@Inject constructor(private val application: Application): ViewModel() {

    @Inject
    lateinit var repository: CartRepository
    val carts = MutableLiveData<ArrayList<Cart>>()
    val pairTotalCart = MutableLiveData<Pair<String?, Double>>()
    private var shoppingCarts = ArrayList<Cart>()
    private lateinit var exchangeRate: ExchangeRate

    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(application).build().inject(this)
    }

    fun setShoppingCart(json: String) {
        val listType = object : TypeToken<ArrayList<Cart>>() {}.type
        Gson().fromJson<ArrayList<Cart>>(json, listType)?.let {
            processShoppingCartWithRate(it)
        }
    }

    fun getExchangeRates() {
        repository.getExchangeRates(BuildConfig.API_KEY, ::processExchangeRates)
    }

    fun setExchangeRate(codeCurrency: String) {
        var total = 0.0
        if (!this::exchangeRate.isInitialized) return
        val defaultRate = exchangeRate.rates["USD"]
        val toRate = exchangeRate.rates[codeCurrency]
        val currency = repository.getCurrencies().firstOrNull { it.code == codeCurrency }
        shoppingCarts.forEach { cart ->
            cart.totalAmountByCurrency = ExchangeUtils.currencyConverter(cart.totalAmount(), defaultRate!!, toRate!!)
            cart.rateFormat = currency
            total+=cart.totalAmountByCurrency
        }
        carts.value = shoppingCarts
        pairTotalCart.value = Pair(currency?.locale, total)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        exchangeRate?.let {
            this.exchangeRate = it
        }
        setExchangeRate("BRL")
    }

    private fun processShoppingCartWithRate(value: ArrayList<Cart>) {
        shoppingCarts = value
    }

    override fun onCleared() {
        repository.clear()
        super.onCleared()
    }

    fun clear() {
        onCleared()
    }
}