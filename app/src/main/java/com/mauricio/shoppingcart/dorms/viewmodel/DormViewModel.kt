package com.mauricio.shoppingcart.dorms.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class DormViewModel @Inject constructor(private val repository: DormRepository, private val application: Application): ViewModel() {

    private val _dorms = MutableLiveData<ArrayList<Dorm>>()
    val dorms: LiveData<ArrayList<Dorm>> = _dorms

    private val _totalAmount = MutableLiveData<Double>(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean> = _showLoading

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    fun showLoading() {
        _showLoading.postValue(true)
    }

    fun hideLoading() {
        _showLoading.postValue(false)
    }

    fun listDorms() {
        showLoading()
        repository.listDorms(::processListDorms)
    }

    fun addShopping(dorm: Dorm) {
        repository.addShopping(dorm, ::processShopping)
    }

    fun getShopping() = repository.getShoppings()

    private fun processShopping(amount: Double) {
        _totalAmount.value = amount
    }

    private fun processListDorms(values: ArrayList<Dorm>?, e: Throwable?) {
        hideLoading()
        values?.let {
            if (it.size > 0) _dorms.value = it
        }
        e?.let {
            _messageError.value = application.getString(R.string.error_operation)
        }
    }
}