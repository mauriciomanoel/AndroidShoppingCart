package com.mauricio.shoppingcart.exchange.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.exchange.models.ExchangeRate

@Database(entities = [ExchangeRate::class, CurrencyRate::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ExchangeRateRoomDB: RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}