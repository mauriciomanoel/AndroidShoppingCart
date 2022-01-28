package com.mauricio.shoppingcart.di.module

import android.content.Context
import androidx.room.Room
import com.mauricio.shoppingcart.exchange.repository.ExchangeRateDao
import com.mauricio.shoppingcart.exchange.repository.ExchangeRateRoomDB
import com.mauricio.shoppingcart.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideExchangeRateDao(appDatabase: ExchangeRateRoomDB): ExchangeRateDao {
        return appDatabase.exchangeRateDao()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): ExchangeRateRoomDB {
        return Room.databaseBuilder(
            context.applicationContext,
            ExchangeRateRoomDB::class.java,
            Constant.DATABASE_NAME
        ).build()
    }
}