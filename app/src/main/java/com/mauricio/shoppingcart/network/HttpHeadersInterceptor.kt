package com.mauricio.shoppingcart.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpHeadersInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        lastUrlRequest = request.url().toString()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .build()
        return chain.proceed(request)
    }

    companion object {
        var lastUrlRequest: String? = null
    }
}