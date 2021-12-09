package com.mauricio.shoppingcart.network

import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApiService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("v1/latest")
    fun getExchangeRates(@Query("access_key") accessKey: String): Observable<Response<ExchangeRate>>

    @GET
    fun setNetworkStats(@Url url: String, @Query("action") action: String, @Query("duration") duration: Long, @Query("status") status: String): Observable<Response<String>>
}