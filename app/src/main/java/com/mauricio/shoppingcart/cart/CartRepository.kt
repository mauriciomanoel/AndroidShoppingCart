package com.mauricio.shoppingcart.cart

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.exchange.ExchangeRate
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.utils.file.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CartRepository  @Inject constructor(private val apiService: RetrofitApiService, private val application: Application)  {
    private val compositeDisposable = CompositeDisposable()
    private var currencies: ArrayList<Currency>

    init {
        currencies = loadCurrencies()
    }

    private fun loadCurrencies(): ArrayList<Currency> {
        val valueJson = FileUtils.loadFromAsset(application.baseContext, "currencies.json")
        val listType = object : TypeToken<ArrayList<Currency>>() {}.type
        val value = Gson().fromJson<ArrayList<Currency>>(valueJson, listType)
        value.sortBy { it.code }
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

    fun getCurrencies(): ArrayList<Currency> {
        return currencies
    }
    fun clear() {
        compositeDisposable.clear()
    }

}