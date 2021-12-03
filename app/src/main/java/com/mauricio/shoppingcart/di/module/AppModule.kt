package com.mauricio.shoppingcart.di.module

import android.app.Application
import com.mauricio.shoppingcart.cart.CartRepository
import com.mauricio.shoppingcart.cart.CartViewModel
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideRepository(apiService: RetrofitApiService, application: Application) = DormRepository(apiService, application)    @Provides
    @Singleton
    fun provideCartRepository(apiService: RetrofitApiService) = CartRepository(apiService)

    @Provides
    fun provideDormViewModel(application: Application) = DormViewModel(application)

    @Provides
    fun provideCartViewModel(application: Application) = CartViewModel(application)
}
