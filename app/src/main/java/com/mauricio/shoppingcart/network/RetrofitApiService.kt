package com.mauricio.shoppingcart.network

import com.mauricio.shoppingcart.cart.ExchangeRate
import io.reactivex.Observable
import retrofit2.http.*

interface RetrofitApiService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("v1/latest")
    fun getExchangeRates(@Query("access_key") accessKey: String): Observable<ExchangeRate>

}