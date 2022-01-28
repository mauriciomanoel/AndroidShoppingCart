package com.mauricio.shoppingcart.cart.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="Currency")
data class Currency (
    @PrimaryKey
    val code: String,
    val name: String,
    val defaultFractionDigits: Int,
    val locale: Locale?
)