package com.mauricio.shoppingcart.exchange.repository

import android.util.Log
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.di.module.NetworkModule.BASE_URL_NETWORK_STATS
import com.mauricio.shoppingcart.di.module.NetworkModule.STATUS_ERROR
import com.mauricio.shoppingcart.di.module.NetworkModule.STATUS_SUCCESS
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.network.ErrorResult
import com.mauricio.shoppingcart.network.RetrofitApiService
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*
import java.util.Base64.getEncoder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ExchangeRepository @Inject constructor(private val apiService: RetrofitApiService, private val exchangeRateDao: ExchangeRateDao)  {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val jobs: MutableList<Job> = mutableListOf()

    fun getExchangeRates(
        process: (value: ExchangeRate?, e: Throwable?) -> Unit
    ) {

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "CoroutineExceptionHandler got $exception")
        }
        val job = coroutineScope.launch(handler) {
            val exchangeRates = apiService.getExchangeRates()
            addExchangeRateLocal(exchangeRates)
        }
        jobs.add(job)
        job.invokeOnCompletion { exception: Throwable? ->
            exception?.let {
                Log.e(TAG, "JobCancellationException got $exception")
            }
        }

        jobs.add(CoroutineScope(Dispatchers.IO).async {
            exchangeRateDao.getExchangeRate().get(0).let {
                process(it, null)
            }
        })
    }

    private fun setNetworkStats(duration: Long, status: String) {
        val action: String = getEncoder().encodeToString(AndroidShoppingCartApplication.lastUrlRequest?.toByteArray())
//        Log.v(TAG, "response time2 : ${duration} ms | url= ${AndroidShoppingCartApplication.lastUrlRequest}")
//        apiService.setNetworkStats(BASE_URL_NETWORK_STATS, action, duration, status)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe{}
    }


    fun getListCurrencyRate(process: (values: List<CurrencyRate>) -> Unit) {
        jobs.add(coroutineScope.launch {
            process(exchangeRateDao.getCurrencyRate())
        })
    }

    private fun addExchangeRateLocal(values: ExchangeRate) {
        val currencies = ArrayList<CurrencyRate>()
        values.rates.forEach { (s, d) ->
            val currency = Currency.getInstance(s)
            var locale = NumberFormat.getAvailableLocales().firstOrNull {
                currency.currencyCode == NumberFormat.getCurrencyInstance(it).currency.currencyCode
            }
            if (locale == null) {
                locale = Locale.getDefault()
            }
            currencies.add(CurrencyRate(code = currency.currencyCode, locale = locale))
        }

        jobs.add(coroutineScope.launch {
            exchangeRateDao.insert(values)
            currencies.forEach {
                exchangeRateDao.insert(it)
            }
        })
    }

    fun clear() {
        jobs.forEach {
            if (it.isActive) it.cancel()
        }
    }

    companion object {
        val TAG: String = ExchangeRepository::class.java.canonicalName
    }

}