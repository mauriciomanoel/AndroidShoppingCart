package com.mauricio.shoppingcart.dorms.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.utils.file.FileUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DormRepository @Inject constructor(private val application: Context)  {
    private var dorms: ArrayList<Dorm>
    private val shoppings = ArrayList<Cart>()

    init {
        dorms = loadDorms()
    }

    private fun loadDorms(): ArrayList<Dorm> {
        val valueJson = FileUtils.loadFromAsset(application, "dorms.json")
        val listType = object : TypeToken<ArrayList<Dorm>>() {}.type
        val value = Gson().fromJson<ArrayList<Dorm>>(valueJson, listType)
        return value
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

    companion object {
        val TAG: String = DormRepository::class.java.simpleName
    }
}