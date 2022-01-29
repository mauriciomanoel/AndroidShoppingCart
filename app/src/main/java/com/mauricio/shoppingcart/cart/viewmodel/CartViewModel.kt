package com.mauricio.shoppingcart.cart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mauricio.shoppingcart.cart.repository.CartRepository
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import com.mauricio.shoppingcart.utils.Constant.DEFAULT_CURRENCY_CODE
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CartViewModel @Inject constructor(private val exchangeRepository: ExchangeRepository): ViewModel() {

    private val _carts = MutableLiveData<List<Cart>>()
    val carts: LiveData<List<Cart>> = _carts

    private val _pairTotalCart = MutableLiveData<Pair<CurrencyRate, Double>>()
    val pairTotalCart: LiveData<Pair<CurrencyRate, Double>> = _pairTotalCart

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    private var shoppingCarts = ArrayList<Cart>()
    private var codeCurrency: CurrencyRate = DEFAULT_CURRENCY_CODE
    var exchangeRate: ExchangeRate? = null

    override fun onCleared() {
        exchangeRepository.clear()
        super.onCleared()
    }

    fun setShoppingCart(value: ArrayList<Cart>) {
        shoppingCarts = value
    }

    fun getExchangeRates() {
        showLoading()
        exchangeRepository.getExchangeRates(::processExchangeRates)
    }

    fun setExchangeRate(codeCurrency: CurrencyRate) {
        this.codeCurrency = codeCurrency
        getExchangeRates()
    }

    private fun calculateCurrencyPerCart(codeCurrency: CurrencyRate) {
        var total = 0.0
        var defaultRate: Double? = null
        var toRate: Double? = null

        if (shoppingCarts.size == 0) return
        exchangeRate?.let {
            defaultRate = it.rates[DEFAULT_CURRENCY_CODE.code]
            toRate = it.rates[codeCurrency.code]
        }

        shoppingCarts.forEach { cart ->
            if (defaultRate != null && toRate != null) {
                cart.totalAmountByCurrency = ExchangeUtils.currencyConverter(cart.totalAmount(), defaultRate!!, toRate!!)
            } else {
                cart.totalAmountByCurrency = cart.totalAmount()
            }
            cart.rateFormat = codeCurrency
            total+=cart.totalAmountByCurrency
        }
        _carts.postValue(shoppingCarts)
        _pairTotalCart.postValue(Pair(codeCurrency, total))
    }

    private fun processExchangeRates(value: ExchangeRate?, e: Throwable?) {
        hideLoading()
        value?.let {
            this.exchangeRate = it
        }
        e?.let {
            _messageError.value = e.message
        }
        calculateCurrencyPerCart(this.codeCurrency)
    }

    fun clear() {
        onCleared()
        hideLoading()
    }

    private fun showLoading() {
        _showLoading.postValue(true)
    }

    private fun hideLoading() {
        _showLoading.postValue(false)
    }
}