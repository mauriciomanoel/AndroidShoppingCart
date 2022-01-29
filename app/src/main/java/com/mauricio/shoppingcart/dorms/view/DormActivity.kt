package com.mauricio.shoppingcart.dorms.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.cart.view.CartActivity
import com.mauricio.shoppingcart.databinding.ActivityDormBinding
import com.mauricio.shoppingcart.dorms.adapters.DormRecyclerViewAdapter
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.dorms.models.IOnClickEvent
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import com.mauricio.shoppingcart.exchange.viewmodel.ExchangeViewModel
import com.mauricio.shoppingcart.utils.Constant
import com.mauricio.shoppingcart.utils.Constant.SHOPPING
import com.mauricio.shoppingcart.utils.extensions.formatNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DormActivity : AppCompatActivity(), IOnClickEvent {

    private lateinit var binding: ActivityDormBinding
    private lateinit var dormAdapter: DormRecyclerViewAdapter
    lateinit var behavior: BottomSheetBehavior<View>

    private val viewModel by viewModels<DormViewModel>()
    private val exchangeViewModel by viewModels<ExchangeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dorm)
        binding.lifecycleOwner = this

        setContentView(binding.root)

        initParameters()
        initAdapters()
        initObservers()
        initBottomSheet()
        initListeners()
        viewModel.listDorms()
    }

    private fun initParameters() {
        val defaultValue = 0.0
        binding.totalAmount.text = defaultValue.formatNumber(Constant.DEFAULT_CURRENCY_CODE)
    }

    private fun initObservers() {
        with(viewModel) {
            dorms.observe(this@DormActivity) { list ->
                dormAdapter.differ.submitList(list)
            }
            totalAmount.observe(this@DormActivity) { amount ->
                binding.totalAmount.text = amount.formatNumber(Constant.DEFAULT_CURRENCY_CODE)
                updateStateButtonCheckout(amount)
            }
            viewModel.messageError.observe(this@DormActivity) { message ->
                Toast.makeText(this@DormActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        exchangeViewModel.messageError.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateStateButtonCheckout(value: Double) {
        binding.enableButtonCheckout = value > 0
    }

    private fun initAdapters() {
        dormAdapter = DormRecyclerViewAdapter(this)
        binding.dormAdapter = dormAdapter
    }

    private fun initBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.layoutBottomSheetDorm)
        binding.layoutBottomSheetDorm.setOnClickListener {
            if (behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.expandedButtonSheet = true
            } else {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.expandedButtonSheet = false
            }
        }
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                binding.expandedButtonSheet = newState == BottomSheetBehavior.STATE_EXPANDED
            }
            override fun onSlide(view: View, v: Float) {}
        })
    }

    private fun initListeners() {
        binding.checkoutShopping.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelableArrayList(SHOPPING, viewModel.getShopping())
                putExtras(bundle)
            }
            startActivity(intent)
        }
    }
    override fun onClickAddBed(dorm: Dorm) {
        Log.v("TAG", "$dorm")
        viewModel.addShopping(dorm)
    }
}