package com.mauricio.shoppingcart

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.shoppingcart.cart.viewmodel.CartViewModel
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils

import com.mauricio.shoppingcart.rules.RxImmediateSchedulerRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.*
import org.junit.runners.MethodSorters
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ShoppingUnitTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
        @JvmField
        var exchangeRate: ExchangeRate? = null
        @JvmField
        var dorms: ArrayList<Dorm>? = null
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    @Mock
    lateinit var dormViewModel: DormViewModel
    @Mock
    lateinit var cartViewModel: CartViewModel
    @Mock
    lateinit var mockContext: Application

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dormViewModel = DormViewModel(mockContext)
        cartViewModel = CartViewModel(mockContext)
    }

    @Test
    fun checkIfVariablesShouldNotBeNull() {
        assertNotNull(dormViewModel)
        assertNotNull(cartViewModel)
    }

    @Test
    fun checkTotalDorms() {
        dormViewModel.listDorms()
        assertNotNull(dormViewModel.dorms.value)
        assertEquals(3, dormViewModel.dorms.value?.size)
    }

    @Test
    fun checkExchangeRates() {
        cartViewModel.getExchangeRates()
        assertNotNull(cartViewModel.exchangeRate)
        assertEquals(168, cartViewModel.exchangeRate?.rates?.size)
        exchangeRate = cartViewModel.exchangeRate
    }

    @Test
    fun checkShoppingAndConverterDollarToBrazilianReal() {
        dormViewModel.listDorms()
        dorms = dormViewModel.dorms.value
        assertNotNull(dorms)
        val dorm = dorms?.get(0)!!
        assertNotNull(dorm)

        cartViewModel.getExchangeRates()
        exchangeRate = cartViewModel.exchangeRate
        assertNotNull(exchangeRate)
        val rateDefault = exchangeRate?.rates?.get("USD")!!
        val rateTo = exchangeRate?.rates?.get("BRL")!!
        val totalBed = 2
        val value = ExchangeUtils.currencyConverter(dorm.pricePerBed.times(totalBed), rateDefault, rateTo)
        val carts = arrayListOf(
            Cart(dorm.id, "Bed dorm ${dorm.id}", dorm.pricePerBed, totalBed)
        )

        cartViewModel.setShoppingCart(carts)
        cartViewModel.setExchangeRate("BRL")
        assertEquals(cartViewModel.pairTotalCart.value?.second!!, value, 0.01)
    }

    @Test
    fun checkShoppingAndConverterDollarToEuro() {
        dormViewModel.listDorms()
        dorms = dormViewModel.dorms.value
        assertNotNull(dorms)
        val dorm = dorms?.get(1)!!
        assertNotNull(dorm)

        cartViewModel.getExchangeRates()
        exchangeRate = cartViewModel.exchangeRate
        assertNotNull(exchangeRate)
        val rateDefault = exchangeRate?.rates?.get("USD")!!
        val rateTo = exchangeRate?.rates?.get("EUR")!!
        val totalBed = 2
        val value = ExchangeUtils.currencyConverter(dorm.pricePerBed.times(totalBed), rateDefault, rateTo)
        val carts = arrayListOf(
            Cart(dorm.id, "Bed dorm ${dorm.id}", dorm.pricePerBed, totalBed)
        )
        cartViewModel.setShoppingCart(carts)
        cartViewModel.setExchangeRate("EUR")
        assertEquals(cartViewModel.pairTotalCart.value?.second!!, value, 0.01)
    }
}