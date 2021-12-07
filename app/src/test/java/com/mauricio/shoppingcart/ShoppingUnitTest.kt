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
import junit.framework.Assert.*
import org.junit.*
import org.junit.runners.MethodSorters
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.math.round

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
    fun checkConverterDollarToBrazilianReal() {
        cartViewModel.getExchangeRates()
        exchangeRate = cartViewModel.exchangeRate
        assertEquals(168, exchangeRate?.rates?.size)
        val rateDefault = exchangeRate?.rates?.get("USD")!!
        val rateTo = exchangeRate?.rates?.get("BRL")!!
        assertTrue(rateDefault > 0.0)
        assertTrue(rateTo > 0.0)
        val value = ExchangeUtils.currencyConverter(17.56, rateDefault, rateTo)
        assertEquals(value, 99.91, 0.01)
    }

    @Test
    fun checkShoppingAndConverterDollarToBrazilianReal() {
        dormViewModel.listDorms()
        dorms = dormViewModel.dorms.value
        assertNotNull(dorms)
        val carts = arrayListOf(
            Cart(dorms?.get(0)?.id!!, "Bed dorm ${dorms?.get(0)?.id}", dorms?.get(0)?.pricePerBed!!, 2)
        )
        cartViewModel.setShoppingCart(carts)
        cartViewModel.setExchangeRate("BRL")
        assertEquals(cartViewModel.pairTotalCart.value?.second!!, 199.82, 0.01)
    }

    @Test
    fun checkShoppingAndConverterDollarToEuro() {
        dormViewModel.listDorms()
        dorms = dormViewModel.dorms.value
        assertNotNull(dorms)
        val carts = arrayListOf(
            Cart(dorms?.get(1)?.id!!, "Bed dorm ${dorms?.get(1)?.id}", dorms?.get(1)?.pricePerBed!!, 2)
        )
        cartViewModel.setShoppingCart(carts)
        cartViewModel.setExchangeRate("EUR")
        assertEquals(cartViewModel.pairTotalCart.value?.second!!, 25.75, 0.01)
    }
}