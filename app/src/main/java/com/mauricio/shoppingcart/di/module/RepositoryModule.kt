package com.mauricio.shoppingcart.di.module

import android.app.Application
import android.content.Context
import com.mauricio.shoppingcart.cart.repository.CartRepository
import com.mauricio.shoppingcart.cart.viewmodel.CartViewModel
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import com.mauricio.shoppingcart.exchange.viewmodel.ExchangeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideRepository(@ApplicationContext context: Context) = DormRepository(context)

    @Provides
    @ViewModelScoped
    fun provideCartRepository(apiService: RetrofitApiService) = CartRepository(apiService)

    @Provides
    @ViewModelScoped
    fun provideExchangeRepository(apiService: RetrofitApiService) = ExchangeRepository(apiService)

}
