package com.mauricio.shoppingcart.exchange.views

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.databinding.FragmentItemListBinding
import com.mauricio.shoppingcart.exchange.adapters.ExchangeRateRecyclerViewAdapter
import com.mauricio.shoppingcart.exchange.models.IOnClickSelectCurrency
import com.mauricio.shoppingcart.exchange.viewmodel.ExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.mauricio.shoppingcart.cart.models.CurrencyRate

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class ExchangeRateFragment : DialogFragment(), IOnClickSelectCurrency {

    lateinit var binding: FragmentItemListBinding
    var callback: IOnClickSelectCurrency? = null

    private val viewModel by viewModels<ExchangeViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? IOnClickSelectCurrency
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, container, false)
        binding.imageViewClose.setOnClickListener { dismiss() }

        initListeners()
        viewModel.getListCurrencyRate()
        return binding.root
    }

    fun initListeners() {
        viewModel.listCurrencies.observe(viewLifecycleOwner) {
            binding.listExchageRate.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ExchangeRateRecyclerViewAdapter(it, this@ExchangeRateFragment)
            }
        }
    }

    override fun setCurrency(codeCurrency: CurrencyRate) {
        callback?.setCurrency(codeCurrency)
        dismiss()
    }

    companion object {
        val TAG: String = ExchangeRateFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = ExchangeRateFragment()
    }
}