package com.mauricio.shoppingcart.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.databinding.ActivityCartBinding
import com.mauricio.shoppingcart.databinding.ActivityDormBinding
import com.mauricio.shoppingcart.dorms.adapters.DormRecyclerViewAdapter
import com.mauricio.shoppingcart.utils.number.NumberUtils
import com.mauricio.vizcodeassignment.utils.Constant.SHOPPING
import dagger.android.AndroidInjection
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartRecyclerViewAdapter
    @Inject
    lateinit var viewModel: CartViewModel
    val listCarts = ArrayList<Cart?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        intent.getStringExtra(SHOPPING)?.let {
            viewModel.setShoppingCart(it)
        }

        initAdapters()
        initObservers()
        viewModel.getExchangeRates()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    private fun initObservers() {
        viewModel.carts.observe(this, { list ->
            listCarts.addAll(list)
            cartAdapter.notifyDataSetChanged()
        })
        viewModel.pairTotalCart.observe(this, { pair->
            binding.totalAmount.text = NumberUtils.formatNumber(pair.second, pair.first)
        })
    }

    private fun initAdapters() {
        cartAdapter = CartRecyclerViewAdapter(listCarts)
        binding.cartAdapter = cartAdapter
    }
}