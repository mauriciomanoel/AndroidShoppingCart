package com.mauricio.shoppingcart.network

import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import retrofit2.http.*

interface RetrofitApiService {

    @GET("v1/latest")
    suspend fun getExchangeRates(@Query("access_key") accessKey: String = BuildConfig.API_KEY): ExchangeRate

    @GET
    suspend fun setNetworkStats(@Url url: String, @Query("action") action: String, @Query("duration") duration: Long, @Query("status") status: String): String
}