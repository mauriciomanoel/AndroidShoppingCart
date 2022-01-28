package com.mauricio.shoppingcart.exchange.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="ExchangeRate")
data class ExchangeRate (
    @PrimaryKey
    val timestamp: Long,
    val success: Boolean,
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    var timestampResponse: Long
)


