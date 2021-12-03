package com.mauricio.shoppingcart.cart

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.dorms.model.Dorm
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.shopping.model.Shopping
import com.mauricio.shoppingcart.utils.file.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CartRepository  @Inject constructor(private val apiService: RetrofitApiService)  {

    fun getExchangeRates(
        key: String,
        process: (value: ExchangeRate?, e: Throwable?) -> Unit
    ) {
        apiService.getExchangeRates(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ response ->
                process(response, null)
            }, {
                    e-> process(null, e)
            })
    }


}