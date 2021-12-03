package com.mauricio.shoppingcart.di.component

import android.app.Application
import com.mauricio.shoppingcart.AndroidShoppingCartApplication
import com.mauricio.shoppingcart.cart.CartViewModel
import com.mauricio.shoppingcart.di.module.ActivityBindings
import com.mauricio.shoppingcart.di.module.AppModule
import com.mauricio.shoppingcart.di.module.NetworkModule
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ActivityBindings::class, AndroidInjectionModule::class])
interface AppComponent : AndroidInjector<AndroidShoppingCartApplication> {
    fun inject(application: Application)
    fun inject(viewModel: DormViewModel)
    fun inject(viewModel: CartViewModel)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(application: Application): Builder
        fun build(): AppComponent
    }
}
