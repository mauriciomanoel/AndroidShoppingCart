package com.mauricio.shoppingcart.dorms.repository

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.dorms.model.Dorm
import com.mauricio.shoppingcart.network.RetrofitApiService
import com.mauricio.shoppingcart.shopping.model.Shopping
import com.mauricio.shoppingcart.utils.file.FileUtils
import javax.inject.Inject

class DormRepository @Inject constructor(private val apiService: RetrofitApiService, private val application: Application)  {
    private lateinit var dorms: ArrayList<Dorm>
    private val shoppings = HashSet<Shopping>()

    init {
        dorms = loadDorms()
    }
    private fun loadDorms(): ArrayList<Dorm> {
        val valueJson = FileUtils.loadFromAsset(application.baseContext, "dorms.json")
        val listType = object : TypeToken<ArrayList<Dorm>>() {}.type
        val value = Gson().fromJson<ArrayList<Dorm>>(valueJson, listType)
        return value
    }

    fun listDorms(process: (value: ArrayList<Dorm>?, e: Throwable?) -> Unit) {
        process(dorms, null)
    }

    fun addShopping(dorm: Dorm, process: (totalAmount: Double) -> Unit) {
        var amount = 0.0
        val item = shoppings.firstOrNull { it.item == dorm.id }
        if (item == null) {
            val shopping = Shopping(item = dorm.id, description = "${dorm.maxBed}-bed dorm", price=dorm.pricePerBed, amount = dorm.getTotalBed())
            shoppings.add(shopping)
        } else {
            item.amount = dorm.getTotalBed()
        }


        shoppings.forEach {
            Log.v("TAG", "${it.amount} - ${it.totalAmount()}")
            amount += it.totalAmount()
        }
        process(amount)
    }

    companion object {
        val TAG: String = DormRepository::class.java.simpleName
    }
}