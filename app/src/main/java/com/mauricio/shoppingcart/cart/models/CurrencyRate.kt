package com.mauricio.shoppingcart.cart.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import java.util.Currency

@Entity(tableName="currency_rate")
data class CurrencyRate(
    @PrimaryKey
    val code: String,
    val locale: Locale?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readSerializable() as Locale?
    )

    fun getNameFormated(): String {
        val curreny = Currency.getInstance(code)
        return "$code - ${curreny.displayName}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeSerializable(locale)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrencyRate> {
        override fun createFromParcel(parcel: Parcel): CurrencyRate {
            return CurrencyRate(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyRate?> {
            return arrayOfNulls(size)
        }
    }
}