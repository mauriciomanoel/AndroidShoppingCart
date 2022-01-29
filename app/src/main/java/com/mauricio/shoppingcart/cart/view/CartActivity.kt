package com.mauricio.shoppingcart.cart.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mauricio.shoppingcart.R
import com.mauricio.shoppingcart.cart.adapters.CartRecyclerViewAdapter
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.cart.models.CurrencyRate
import com.mauricio.shoppingcart.cart.viewmodel.CartViewModel
import com.mauricio.shoppingcart.databinding.ActivityCartBinding
import com.mauricio.shoppingcart.exchange.models.IOnClickSelectCurrency
import com.mauricio.shoppingcart.exchange.views.ExchangeRateFragment
import com.mauricio.shoppingcart.utils.Constant
import com.mauricio.shoppingcart.utils.Constant.SHOPPING
import com.mauricio.shoppingcart.utils.extensions.formatNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity(), IOnClickSelectCurrency {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartRecyclerViewAdapter
    private val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        getShoppingCartFromIntent()?.let {
            viewModel.setShoppingCart(it as ArrayList<Cart>)
        }

        initParameters()
        initAdapters()
        initObservers()
        viewModel.getExchangeRates()
    }

    private fun getShoppingCartFromIntent() = intent.extras?.get(SHOPPING)

    private fun initParameters() {
        val defaultValue = 0.0
        binding.totalAmountCart.text = defaultValue.formatNumber(Constant.DEFAULT_CURRENCY_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
        val dialog = ExchangeRateFragment.newInstance()
        dialog.arguments = bundle
        dialog.callback = this
        dialog.show(supportFragmentManager, ExchangeRateFragment.TAG)
    }

    private fun initObservers() {
        viewModel.carts.observe(this) { list ->
            cartAdapter.submitList(list)
        }
        viewModel.pairTotalCart.observe(this) { pair ->
            binding.totalAmountCart.text = pair.second.formatNumber(pair.first)
        }
        viewModel.showLoading.observe(this) { showLoading ->
            binding.showLoading = showLoading
        }
        viewModel.messageError.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initAdapters() {
        cartAdapter = CartRecyclerViewAdapter()
        binding.cartAdapter = cartAdapter
    }

    override fun setCurrency(codeCurrency: CurrencyRate) {
        viewModel.setExchangeRate(codeCurrency)
    }
}