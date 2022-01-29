package com.mauricio.shoppingcart.cart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.BR
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.databinding.ItemCartBinding
import com.mauricio.shoppingcart.utils.extensions.formatNumber

class CartRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return (oldItem.item == newItem.item
                    && oldItem.totalAmountByCurrency == newItem.totalAmountByCurrency
                    && oldItem.rateFormat == newItem.rateFormat)
        }
        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Cart>) {
        differ.submitList(list)
        notifyDataSetChanged() // force update
    }

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
        differ.currentList[position].run {
            viewHolder.bind(this)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

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

