package com.mauricio.shoppingcart.cart.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.cart.repository.CartRepository
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.di.component.DaggerAppComponent
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils
import javax.inject.Inject

class CartViewModel@Inject constructor(private val application: Application): ViewModel() {

    @Inject
    lateinit var repository: CartRepository
    val carts = MutableLiveData<ArrayList<Cart>>()
    val pairTotalCart = MutableLiveData<Pair<String?, Double>>()
    private var shoppingCarts = ArrayList<Cart>()
    var exchangeRate: ExchangeRate? = null
    val isFinishedLoadExchangeRate = MutableLiveData<Boolean>(false)
    private var codeCurrency: String = "USD"
    val showLoading = MutableLiveData<Boolean>(false)

    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(application).build().inject(this)
    }

    override fun onCleared() {
        repository.clear()
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
        repository.getExchangeRates(BuildConfig.API_KEY, ::processExchangeRates)
    }

    fun setExchangeRate(codeCurrency: String) {
        this.codeCurrency = codeCurrency
        getExchangeRates()
    }

    fun getCurrenciesToString(): String {
        val gson = Gson()
        return gson.toJson(repository.getCurrencies())
    }

    private fun calculateCurrencyPerCart(codeCurrency: String?) {
        var total = 0.0
        var defaultRate: Double? = null
        var toRate: Double? = null
        var currency = repository.getCurrencies().firstOrNull { it.code == "USD" }
        if (shoppingCarts.size == 0) return
        exchangeRate?.let {
            defaultRate = it.rates["USD"]
            toRate = it.rates[codeCurrency]
            currency = repository.getCurrencies().firstOrNull { it.code == codeCurrency }
        }

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
        pairTotalCart.value = Pair(currency?.locale, total)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        hideLoading()
        exchangeRate?.let {
            this.exchangeRate = it
            isFinishedLoadExchangeRate.value = true
        }
        e?.let {
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