package com.mauricio.shoppingcart

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidShoppingCartApplication: Application() {
    companion object {
        var lastUrlRequest: String? = null
    }
}