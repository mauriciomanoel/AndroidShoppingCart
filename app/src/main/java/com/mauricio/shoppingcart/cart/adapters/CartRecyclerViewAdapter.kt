package com.mauricio.shoppingcart.cart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.BR
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.databinding.ItemCartBinding
import com.mauricio.shoppingcart.utils.extensions.formatNumber

class CartRecyclerViewAdapter(private val values: ArrayList<Cart>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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
        values[position].run {
            viewHolder.bind(this)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(var binding: ItemCartBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(cart: Cart) {
            binding.setVariable(BR.id, "ID ${cart.item}")
            binding.setVariable(BR.totalBeds, "Description: ${cart.description}")
            binding.setVariable(BR.totalAmount, "${cart.totalAmountByCurrency.formatNumber(cart.rateFormat!!)}")
            binding.executePendingBindings()
        }
    }
}