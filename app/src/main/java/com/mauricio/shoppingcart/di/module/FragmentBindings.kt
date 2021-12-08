package com.mauricio.shoppingcart.di.module

import com.mauricio.shoppingcart.di.scope.FragmentScope
import com.mauricio.shoppingcart.exchange.views.ExchangeRateFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindings {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideExchangeRateFragment(): ExchangeRateFragment
}
