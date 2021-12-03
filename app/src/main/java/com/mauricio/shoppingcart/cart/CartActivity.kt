package com.mauricio.shoppingcart.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mauricio.shoppingcart.R
import android.util.Log
import com.mauricio.vizcodeassignment.utils.Constant.SHOPPING
import dagger.android.AndroidInjection
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_cart)

        intent.getStringExtra(SHOPPING)?.let {
            Log.v("TAG", it)
        }

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
}