package com.mauricio.shoppingcart.exchange.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.exchange.models.ExchangeRate

@Dao
interface ExchangeRateDao {
    @Query("SELECT * from ExchangeRate order by timestamp")
    fun getExchangeRate(): MutableList<ExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRate)

    @Query("DELETE FROM ExchangeRate")
    fun deleteAll()

    @Query("SELECT * from currency_rate order by code")
    fun getCurrencyRate(): MutableList<CurrencyRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyRate: CurrencyRate)
}