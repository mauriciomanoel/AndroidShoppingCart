package com.mauricio.shoppingcart.exchange.repository

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.utils.extensions.fromGson
import com.mauricio.shoppingcart.utils.extensions.toGson
import java.lang.reflect.Type
import java.util.*

class Converters {

    companion object {

        @TypeConverter
        @JvmStatic
        fun toString(value: Map<String, Double>?) = value?.toGson()

        @TypeConverter
        @JvmStatic
        fun toListOfObjectsImage(value: String?): Map<String, Double>? {
            return value?.fromGson(Map::class.java) as Map<String, Double>?
        }

        @TypeConverter
        @JvmStatic
        fun toString(value: Currency?) = value?.toGson()

        @TypeConverter
        @JvmStatic
        fun toListOfObjectsCurrency(valueCurrency: String?): Currency? {
            return valueCurrency?.fromGson(Currency::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun toString(value: Locale?) = value?.toGson()

        @TypeConverter
        @JvmStatic
        fun toListOfObjectsLocale(value: String?): Locale? {
            return value?.fromGson(Locale::class.java)
        }

    }
}