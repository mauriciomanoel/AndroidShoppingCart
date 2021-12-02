package com.mauricio.shoppingcart.dorms.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.databinding.ActivityDormBinding
import com.mauricio.shoppingcart.dorms.adapters.DormRecyclerViewAdapter
import com.mauricio.shoppingcart.dorms.model.Dorm
import com.mauricio.shoppingcart.dorms.model.IOnClickEvent
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import com.mauricio.shoppingcart.utils.number.NumberUtils
import dagger.android.AndroidInjection
import javax.inject.Inject

class DormActivity : AppCompatActivity(), IOnClickEvent {

    private lateinit var binding: ActivityDormBinding
    private lateinit var dormAdapter: DormRecyclerViewAdapter
    lateinit var behavior: BottomSheetBehavior<View>

    val listDorms: ArrayList<Dorm?> = ArrayList()

    @Inject
    lateinit var viewModel: DormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dorm)
        binding.lifecycleOwner = this

        setContentView(binding.root)

        initAdapters()
        initObservers()
        initBottomSheet()
        viewModel.listDorms()
    }

    private fun initObservers() {
        viewModel.dorms.observe(this, { list ->
            listDorms.addAll(list)
            dormAdapter.notifyDataSetChanged()
        })

        viewModel.totalAmount.observe(this, { amount ->
            binding.totalAmount.text = NumberUtils.formatNumber(amount)
        })

    }

    private fun initAdapters() {
        dormAdapter = DormRecyclerViewAdapter(listDorms, this)
        binding.dormAdapter = dormAdapter
    }

    private fun initBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        binding.layoutBottomSheet.setOnClickListener {
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

    override fun setOnClickListener(dorm: Dorm) {
        Log.v("TAG", "$dorm")
        viewModel.addShopping(dorm)
    }
}