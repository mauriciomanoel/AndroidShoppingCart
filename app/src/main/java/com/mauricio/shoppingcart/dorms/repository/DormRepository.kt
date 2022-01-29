package com.mauricio.shoppingcart.dorms.repository

import android.util.Log
import com.google.gson.Gson
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.cart.models.Cart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class DormRepository @Inject constructor() {
    private var dorms: ArrayList<Dorm>
    private val shoppings = ArrayList<Cart>()

    init {
        dorms = loadDorms()
    }

    private fun loadDorms(): ArrayList<Dorm> {
        val values = ArrayList<Dorm>()
        values.add(Dorm(id= 1, maxBed = 6, pricePerBed = 17.56))
        values.add(Dorm(id= 2, maxBed = 8, pricePerBed = 14.50))
        values.add(Dorm(id= 3, maxBed = 12, pricePerBed = 12.01))
        return values
    }

    fun listDorms(process: (value: ArrayList<Dorm>?, e: Throwable?) -> Unit) {
        process(dorms, null)
    }

    fun addShopping(dorm: Dorm, process: (totalAmount: Double) -> Unit) {
        var amount = 0.0
        val shopping = shoppings.firstOrNull { it.item == dorm.id }
        if (shopping == null) {
            val newShopping = Cart(item = dorm.id, description = "${dorm.maxBed}-bed dorm", price=dorm.pricePerBed, totalItem = dorm.getTotalBed())
            shoppings.add(newShopping)
        } else {
            shopping.totalItem = dorm.getTotalBed()
        }

        if (shopping?.totalItem == 0) {
           val a = shoppings.remove(shopping)
            Log.v("TAG", "${a}")
        }

        shoppings.forEach {
            Log.v("TAG", "${it.totalItem} - ${it.totalAmount()}")
            amount += it.totalAmount()
        }
        process(amount)
    }

    fun getShoppingInString(): String {
        val gson = Gson()
        return gson.toJson(shoppings)
    }

    fun getShoppings() = shoppings

    companion object {
        val TAG: String = DormRepository::class.java.simpleName
    }
}