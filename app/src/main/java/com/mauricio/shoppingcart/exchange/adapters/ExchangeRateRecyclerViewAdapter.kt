package com.mauricio.shoppingcart.exchange.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mauricio.shoppingcart.cart.models.Currency
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.databinding.FragmentItemBinding
import com.mauricio.shoppingcart.exchange.models.IOnClickSelectCurrency

class ExchangeRateRecyclerViewAdapter(
    private val values: List<CurrencyRate>, private val callback: IOnClickSelectCurrency
) : RecyclerView.Adapter<ExchangeRateRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameCurrency.text = item.getNameFormated()
        holder.nameCurrency.setOnClickListener {
            callback.setCurrency(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameCurrency: TextView = binding.nameCurrency
    }

}