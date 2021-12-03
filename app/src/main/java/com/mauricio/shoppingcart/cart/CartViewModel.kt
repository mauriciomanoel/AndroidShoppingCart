package com.mauricio.shoppingcart.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.di.component.DaggerAppComponent
import javax.inject.Inject

class CartViewModel@Inject constructor(private val application: Application): ViewModel() {

    @Inject
    lateinit var repository: CartRepository
    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(application).build().inject(this)
    }

    fun getExchangeRates() {
        repository.getExchangeRates(BuildConfig.API_KEY, ::processExchangeRates)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        Log.v("TAG", "$exchangeRate")
    }
}