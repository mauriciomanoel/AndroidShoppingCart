package com.mauricio.shoppingcart

import android.app.Application
import com.mauricio.shoppingcart.di.component.DaggerAppComponent
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class AndroidShoppingCartApplication: Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .app(this)
            .build().inject(this)
    }
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        var exchangeRate: ExchangeRate? = null
        var lastUrlRequest: String? = null
    }
}