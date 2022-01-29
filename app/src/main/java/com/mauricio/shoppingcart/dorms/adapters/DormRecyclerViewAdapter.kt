package com.mauricio.shoppingcart.dorms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.shoppingcart.BR
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.databinding.ItemDormBinding
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.dorms.models.IOnClickEvent
import com.mauricio.shoppingcart.utils.extensions.formatNumber

class DormRecyclerViewAdapter(private val callback: IOnClickEvent) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val differ = AsyncListDiffer(this, DormDiffCallback())


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
        differ.currentList[position].run {
            viewHolder.binding.addBed.setOnClickListener {
                this.addBed()
                viewHolder.binding.totalBed.text = "${this.getTotalBed()}"
                callback.onClickAddBed(this)
            }
            viewHolder.binding.removeBed.setOnClickListener {
                this.removeBed()
                viewHolder.binding.totalBed.text = "${this.getTotalBed()}"
                callback.onClickAddBed(this)
            }
            viewHolder.bind(this)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(var binding: ItemDormBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(dorm: Dorm) {
            binding.setVariable(BR.id, "Dorm ${dorm.id}")
            binding.setVariable(BR.maxBed, "${dorm.maxBed}-bed dorm")
            binding.setVariable(BR.pricePerBed, "${dorm.pricePerBed.formatNumber()}/Bed")
            binding.executePendingBindings()
        }
    }
}

class DormDiffCallback: DiffUtil.ItemCallback<Dorm>() {
    override fun areItemsTheSame(oldItem: Dorm, newItem: Dorm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Dorm, newItem: Dorm): Boolean {
        return oldItem == newItem
    }
}