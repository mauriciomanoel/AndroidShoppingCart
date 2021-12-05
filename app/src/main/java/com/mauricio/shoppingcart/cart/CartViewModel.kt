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
import com.mauricio.shoppingcart.utils.file.FileUtils
import javax.inject.Inject

class CartViewModel@Inject constructor(private val application: Application): ViewModel() {

    @Inject
    lateinit var repository: CartRepository
    val carts = MutableLiveData<ArrayList<Cart>>()


    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(application).build().inject(this)
    }

    fun setShoppingCart(json: String) {
        val listType = object : TypeToken<ArrayList<Cart>>() {}.type
        Gson().fromJson<ArrayList<Cart>>(json, listType)?.let {
            carts.value = it
            Log.v("TAG", it.toString())
        }
    }

    fun getExchangeRates() {
        repository.getExchangeRates(BuildConfig.API_KEY, ::processExchangeRates)
    }

    private fun processExchangeRates(exchangeRate: ExchangeRate?, e: Throwable?) {
        //Log.v("TAG", "$exchangeRate")
    }

    override fun onCleared() {
        repository.clear()
        super.onCleared()
    }

    fun clear() {
        onCleared()
    }
}