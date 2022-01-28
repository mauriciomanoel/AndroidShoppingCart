package com.mauricio.shoppingcart.utils.extensions

import com.google.gson.Gson

fun <T> String.fromGson(classOfT: Class<T>) = Gson().fromJson(this, classOfT)

fun Any.toGson() = Gson().toJson(this)
