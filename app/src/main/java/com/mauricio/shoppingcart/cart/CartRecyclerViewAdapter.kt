package com.mauricio.shoppingcart.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.BR
import com.mauricio.shoppingcart.databinding.ItemCartBinding
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils

class CartRecyclerViewAdapter(private val cartList: ArrayList<Cart?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCartBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cart,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolder = holder as ViewHolder
        cartList[position]?.let { cart ->
//            viewHolder.binding.addBed.setOnClickListener {
//                dorm.addBed()
//                viewHolder.binding.totalBed.text = "${dorm.getTotalBed()}"
//                callback.onClickAddBed(dorm)
//            }
//            viewHolder.binding.removeBed.setOnClickListener {
//                dorm.removeBed()
//                viewHolder.binding.totalBed.text = "${dorm.getTotalBed()}"
//                callback.onClickAddBed(dorm)
//            }
            Log.v("TAG: CartRecyclerViewAdapter", "${cart.totalAmount()}")

            viewHolder.bind(cart)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    inner class ViewHolder(var binding: ItemCartBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(cart: Cart?) {
            binding.setVariable(BR.id, "ID ${cart?.item}")
            binding.setVariable(BR.totalBeds, "Description: ${cart?.description}")
            binding.setVariable(BR.totalAmount, "Total Amount: ${ExchangeUtils.currencyConverter(cart?.totalAmount(), toCurrency= "BLR", rateDefaultCurrent = 1.131344, rateToCurrent = 6.396321) }")
            binding.executePendingBindings()
//            fun currencyConverter(value: Double?, fromCurrency: String = "USD", toCurrency:String, rateFromCurrent: Double, rateToCurrent: Double): String

        }
    }
}