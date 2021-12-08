package com.mauricio.shoppingcart.cart.repository

import android.app.Application
import com.mauricio.shoppingcart.network.RetrofitApiService
import javax.inject.Inject

class CartRepository  @Inject constructor(private val apiService: RetrofitApiService, private val application: Application)  {

    fun confirmItensCart() {}

}