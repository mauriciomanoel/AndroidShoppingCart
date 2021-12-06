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
import com.mauricio.shoppingcart.utils.number.NumberUtils

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
            viewHolder.bind(cart)
        }
    }

    override fun getItemCount(): Int = cartList.size

    inner class ViewHolder(var binding: ItemCartBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(cart: Cart?) {
            binding.setVariable(BR.id, "ID ${cart?.item}")
            binding.setVariable(BR.totalBeds, "Description: ${cart?.description}")
            binding.setVariable(BR.totalAmount, "${NumberUtils.formatNumber(cart?.totalAmountByCurrency, cart?.rateFormat?.locale)}")
            binding.executePendingBindings()
        }
    }
}