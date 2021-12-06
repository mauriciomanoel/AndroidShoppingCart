package com.mauricio.shoppingcart.dorms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.shoppingcart.BR
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.databinding.ItemDormBinding
import com.mauricio.shoppingcart.dorms.model.Dorm
import com.mauricio.shoppingcart.dorms.model.IOnClickEvent

class DormRecyclerViewAdapter(private val dormList: ArrayList<Dorm?>, private val callback: IOnClickEvent) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemDormBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_dorm,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolder = holder as ViewHolder
        dormList[position]?.let { dorm ->
            viewHolder.binding.addBed.setOnClickListener {
                dorm.addBed()
                viewHolder.binding.totalBed.text = "${dorm.getTotalBed()}"
                callback.onClickAddBed(dorm)
            }
            viewHolder.binding.removeBed.setOnClickListener {
                dorm.removeBed()
                viewHolder.binding.totalBed.text = "${dorm.getTotalBed()}"
                callback.onClickAddBed(dorm)
            }
            viewHolder.bind(dorm)
        }
    }

    override fun getItemCount(): Int = dormList.size

    inner class ViewHolder(var binding: ItemDormBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(dorm: Dorm?) {
            binding.setVariable(BR.id, "Dorm ${dorm?.id}")
            binding.setVariable(BR.maxBed, "${dorm?.maxBed}-bed dorm")
            binding.setVariable(BR.pricePerBed, "${dorm?.pricePerBed}/Bed")
            binding.executePendingBindings()
        }
    }
}