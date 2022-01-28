package com.mauricio.shoppingcart.di.module

import com.mauricio.shoppingcart.cart.repository.CartRepository
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import com.mauricio.shoppingcart.exchange.repository.ExchangeRateDao
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideRepository() = DormRepository()

    @Provides
    @ViewModelScoped
    fun provideCartRepository(apiService: RetrofitApiService) = CartRepository(apiService)

    @Provides
    @ViewModelScoped
    fun provideExchangeRepository(apiService: RetrofitApiService, exchangeRateDao: ExchangeRateDao) = ExchangeRepository(apiService, exchangeRateDao)

}
