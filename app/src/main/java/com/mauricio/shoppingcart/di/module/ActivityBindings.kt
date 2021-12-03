package com.mauricio.shoppingcart.di.module

import com.mauricio.shoppingcart.cart.CartActivity
import com.mauricio.shoppingcart.dorms.view.DormActivity
import com.mauricio.vizcodeassignment.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBindings {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeDormActivity(): DormActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeCartActivity(): CartActivity
}
