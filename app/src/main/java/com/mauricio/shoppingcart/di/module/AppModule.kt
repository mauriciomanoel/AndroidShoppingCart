package com.mauricio.shoppingcart.di.module

import android.app.Application
import com.mauricio.shoppingcart.cart.repository.CartRepository
import com.mauricio.shoppingcart.cart.viewmodel.CartViewModel
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
    fun provideRepository(application: Application) = DormRepository(application)

    @Provides
    @Singleton
    fun provideCartRepository(apiService: RetrofitApiService, application: Application) = CartRepository(apiService, application)

    @Provides
    fun provideDormViewModel(application: Application) = DormViewModel(application)

    @Provides
    fun provideCartViewModel(application: Application) = CartViewModel(application)
}
