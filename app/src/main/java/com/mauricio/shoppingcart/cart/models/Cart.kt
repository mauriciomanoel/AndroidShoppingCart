package com.mauricio.shoppingcart.cart.models

import android.os.Parcel
import android.os.Parcelable

data class Cart (
    var item: Long,
    var description: String,
    var price: Double,
    var totalItem: Int,
    var totalAmountByCurrency: Double=0.0,
    var rateFormat: CurrencyRate?=null): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readParcelable(CurrencyRate::class.java.classLoader)
    )

    fun totalAmount(): Double {
        return price.times(totalItem)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(item)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(totalItem)
        parcel.writeDouble(totalAmountByCurrency)
        parcel.writeParcelable(rateFormat, flags)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }
}


