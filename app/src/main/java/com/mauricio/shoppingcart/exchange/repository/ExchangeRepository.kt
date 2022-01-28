package com.mauricio.shoppingcart.exchange.repository

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.cart.models.Currency
import com.mauricio.shoppingcart.di.module.NetworkModule.BASE_URL_NETWORK_STATS
import com.mauricio.shoppingcart.di.module.NetworkModule.STATUS_ERROR
import com.mauricio.shoppingcart.di.module.NetworkModule.STATUS_SUCCESS
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.network.ErrorResult
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.utils.file.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.Base64.getEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepository @Inject constructor(private val apiService: RetrofitApiService)  {
    private val compositeDisposable = CompositeDisposable()
    private var rates: Map<String, Double>? = null

    init {
//        currencies = loadCurrencies()

    }

//    private fun loadCurrencies(): ArrayList<Currency> {
//        val valueJson = FileUtils.loadFromAsset(application.baseContext, "currencies.json")
//        val listType = object : TypeToken<ArrayList<Currency>>(){}.type
//        val value = Gson().fromJson<ArrayList<Currency>>(valueJson, listType)
//        value.sortBy { it.code }
//        return value
//    }

    fun getExchangeRates(
        process: (value: ExchangeRate?, e: Throwable?) -> Unit
    ) {
        var start = System.currentTimeMillis()
        var stop: Long
        val disposable = apiService.getExchangeRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ response ->
                val duration = response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis()

                if (response.code() == 200) {
                    val exchangeRate = response.body()
                    exchangeRate?.timestampResponse = System.currentTimeMillis().div(1000L)
                    rates = exchangeRate?.rates
                    process(exchangeRate, null)
                    setNetworkStats(duration, STATUS_SUCCESS)
                } else {
                    val listType = object : TypeToken<ErrorResult>(){}.type
                    val value = Gson().fromJson<ErrorResult>(response.errorBody()?.string(), listType)
                    process(null, Exception(value.error.message))
                    setNetworkStats(duration, STATUS_ERROR)
                }
            }, {
                e-> process(null, e)
                stop = System.currentTimeMillis()
                setNetworkStats(stop-start, STATUS_ERROR)
            })
        compositeDisposable.add(disposable)
    }

    private fun setNetworkStats(duration: Long, status: String) {
        val action: String = getEncoder().encodeToString(AndroidShoppingCartApplication.lastUrlRequest?.toByteArray())
//        Log.v(TAG, "response time2 : ${duration} ms | url= ${AndroidShoppingCartApplication.lastUrlRequest}")
        apiService.setNetworkStats(BASE_URL_NETWORK_STATS, action, duration, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{}
    }

    fun getCurrencies(): ArrayList<java.util.Currency> {
        val currency = ArrayList<java.util.Currency>()
        rates?.forEach { s, d ->
            currency.add(java.util.Currency.getInstance(s))
        }
        return currency
    }

    fun clear() = compositeDisposable.clear()

    companion object {
        val TAG: String = ExchangeRepository::class.java.canonicalName
    }

}