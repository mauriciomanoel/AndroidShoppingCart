package com.mauricio.shoppingcart.di.module

import android.app.Application
import com.mauricio.shoppingcart.BuildConfig
import com.mauricio.shoppingcart.network.HttpHeadersInterceptor
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.vizcodeassignment.utils.network.ConnectionNetwork
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class NetworkModule {

    @Module
    companion object {

        const val BASE_URL = "http://api.exchangeratesapi.io/"
        @JvmStatic
        @Singleton
        @Provides
        fun provideHeadersInterceptor(): HttpHeadersInterceptor = HttpHeadersInterceptor()

        @JvmStatic
        @Singleton
        @Provides
        fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        @JvmStatic
        @Singleton
        @Provides
        fun provideOkHttp(
            loggingInterceptor: HttpLoggingInterceptor,
            headersInterceptor: HttpHeadersInterceptor,
            cache: Cache,
            context: Application
        ): OkHttpClient = OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(headersInterceptor)
                        .cache(cache)
                        .addInterceptor { chain ->
                            var request = chain.request()
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()

                            chain.proceed(request)
                        }
                        .build()

        @Singleton
        @JvmStatic
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
                Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()

        @JvmStatic
        @Singleton
        @Provides
        fun provideApiService(retrofit: Retrofit): RetrofitApiService = retrofit.create(RetrofitApiService::class.java)

        @JvmStatic
        @Singleton
        @Provides
        fun provideCacheFile(context: Application): Cache {
            val cacheSize = (5 * 1024 * 1024).toLong()
            return Cache(context.cacheDir, cacheSize)
        }
    }
}
