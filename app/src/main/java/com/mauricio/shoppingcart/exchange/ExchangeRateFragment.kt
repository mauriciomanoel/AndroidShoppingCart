package com.mauricio.shoppingcart.exchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.cart.Currency
import com.mauricio.shoppingcart.databinding.FragmentItemListBinding

/**
 * A fragment representing a list of Items.
 */
class ExchangeRateFragment : DialogFragment(), IOnClickSelectCurrency {

    lateinit var binding: FragmentItemListBinding
    var callback: IOnClickSelectCurrency? = null
    var listCurrency = ArrayList<Currency>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listCurrency = loadCurrencies(it.getString(KEY_BUNDLE))
        }
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

        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ExchangeRateRecyclerViewAdapter(listCurrency, this@ExchangeRateFragment)
        }

        binding.imageViewClose.setOnClickListener { dismiss() }
        return binding.root
    }

    companion object {
        val KEY_BUNDLE: String = "KEY_BUNDLE"
        val TAG: String = ExchangeRateFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = ExchangeRateFragment()
    }

    override fun setCurrency(codeCurrency: String) {
        callback?.setCurrency(codeCurrency)
        dismiss()
    }

    private fun loadCurrencies(value: String?): ArrayList<Currency> {
        val listType = object : TypeToken<ArrayList<Currency>>() {}.type
        val value = Gson().fromJson<ArrayList<Currency>>(value, listType)
        return value
    }
}