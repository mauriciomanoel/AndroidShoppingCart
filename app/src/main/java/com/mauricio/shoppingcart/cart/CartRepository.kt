package com.mauricio.shoppingcart.cart

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.dorms.model.Dorm
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.utils.file.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CartRepository  @Inject constructor(private val apiService: RetrofitApiService, private val application: Application)  {
    private val compositeDisposable = CompositeDisposable()
    private var currencies: Currencies

    init {
        currencies = loadCurrencies()
    }

    private fun loadCurrencies(): HashMap<String, Currency> {
        val valueJson = FileUtils.loadFromAsset(application.baseContext, "currencies.json")
        val listType = object : TypeToken<Currencies>() {}.type
        val value = Gson().fromJson<Currencies>(valueJson, listType)
        return value
    }

    fun getExchangeRates(
        key: String,
        process: (value: ExchangeRate?, e: Throwable?) -> Unit
    ) {
        val disposable = apiService.getExchangeRates(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ response ->
                process(response, null)
            }, {
                    e-> process(null, e)
            })
        compositeDisposable.add(disposable)
    }

    fun clear() {
        compositeDisposable.clear()
    }

}