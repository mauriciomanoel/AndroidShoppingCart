package com.mauricio.shoppingcart.cart.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.cart.adapters.CartRecyclerViewAdapter
import com.mauricio.shoppingcart.cart.viewmodel.CartViewModel
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.databinding.ActivityCartBinding
import com.mauricio.shoppingcart.exchange.views.ExchangeRateFragment
import com.mauricio.shoppingcart.exchange.models.IOnClickSelectCurrency
import com.mauricio.shoppingcart.utils.Constant.SHOPPING
import com.mauricio.shoppingcart.utils.number.NumberUtils
import dagger.android.AndroidInjection
import javax.inject.Inject

class CartActivity : AppCompatActivity(), IOnClickSelectCurrency {

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.shopping_cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_change_currency -> {
                openDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    private fun openDialog() {
        val bundle = Bundle()
        bundle.putString(ExchangeRateFragment.KEY_BUNDLE, viewModel.getCurrenciesToString())
        val dialog = ExchangeRateFragment.newInstance()
        dialog.arguments = bundle
        dialog.callback = this
        dialog.show(supportFragmentManager, ExchangeRateFragment.TAG)
    }

    private fun initObservers() {
        viewModel.carts.observe(this, { list ->
            listCarts.clear()
            listCarts.addAll(list)
            cartAdapter.notifyDataSetChanged()
        })
        viewModel.pairTotalCart.observe(this, { pair->
            binding.totalAmountCart.text = NumberUtils.formatNumber(pair.second, pair.first)
        })
        viewModel.showLoading.observe(this, { showLoading ->
            binding.showLoading = showLoading
        })
    }

    private fun initAdapters() {
        cartAdapter = CartRecyclerViewAdapter(listCarts)
        binding.cartAdapter = cartAdapter
    }

    override fun setCurrency(codeCurrency: String) {
        viewModel.setExchangeRate(codeCurrency)
    }
}