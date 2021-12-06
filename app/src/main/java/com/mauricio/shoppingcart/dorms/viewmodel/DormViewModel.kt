package com.mauricio.shoppingcart.dorms.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.di.component.DaggerAppComponent
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import javax.inject.Inject

class DormViewModel@Inject constructor(private val application: Application): ViewModel() {

    @Inject
    lateinit var repository: DormRepository
    val dorms = MutableLiveData<ArrayList<Dorm>>()
    val totalAmount = MutableLiveData<Double>(0.0)
    val showLoading = MutableLiveData<Boolean>(false)
    val messageError = MutableLiveData<String>()

    fun showLoading() {
        showLoading.postValue(true)
    }

    fun hideLoading() {
        showLoading.postValue(false)
    }

    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(application).build().inject(this)
    }

    fun listDorms() {
        showLoading()
        repository.listDorms(::processListDorms)
    }

    fun addShopping(dorm: Dorm) {
        repository.addShopping(dorm, ::processShopping)
    }

    fun getShoppingInString(): String {
        return repository.getShoppingInString()
    }

    private fun processShopping(amount: Double) {
        totalAmount.value = amount
    }

    private fun processListDorms(values: ArrayList<Dorm>?, e: Throwable?) {
        hideLoading()
        values?.let {
            if (it.size > 0) dorms.value = it
        }
        e?.let {
            messageError.value = application.getString(R.string.error_operation)
        }
    }


}