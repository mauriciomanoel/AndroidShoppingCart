package com.mauricio.shoppingcart

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.shoppingcart.cart.viewmodel.CartViewModel
import com.mauricio.shoppingcart.cart.models.Cart
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.dorms.viewmodel.DormViewModel
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import com.mauricio.shoppingcart.dorms.repository.DormRepository
import com.mauricio.shoppingcart.exchange.repository.ExchangeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [29])
@ExperimentalCoroutinesApi
class ShoppingUnitTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)



    @Mock
    lateinit var dormViewModel: DormViewModel
    @Mock
    lateinit var cartViewModel: CartViewModel

    @Inject
    lateinit var mockContext: Application

    @Inject
    lateinit var repository: DormRepository

    @Inject
    lateinit var exchangeRepository: ExchangeRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
        Dispatchers.setMain(StandardTestDispatcher())

        dormViewModel = DormViewModel(repository, mockContext)
        cartViewModel = CartViewModel(exchangeRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun setDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `Check If Variables Should Not Be Null`() = runBlocking {
        assertNotNull(dormViewModel)
        assertNotNull(cartViewModel)
    }

    @Test
    fun `Check Total Dorms`() = runBlocking {
        dormViewModel.listDorms()
        assertNotNull(dormViewModel.dorms.value)
        assertEquals(3, dormViewModel.dorms.value?.size)
    }

//    @Test
//    fun `Check Exchange Rates`() = runBlocking {
//        cartViewModel.getExchangeRates()
//        delay(2000)
//        assertNotNull(cartViewModel.exchangeRate)
//        assertEquals(168, cartViewModel.exchangeRate?.rates?.size)
//    }
//
//    @Test
//    fun checkShoppingAndConverterDollarToBrazilianReal() {
//        dormViewModel.listDorms()
//        dorms = dormViewModel.dorms.value
//        assertNotNull(dorms)
//        val dorm = dorms?.get(0)!!
//        assertNotNull(dorm)
//
//        cartViewModel.getExchangeRates()
//        exchangeRate = cartViewModel.exchangeRate
//        assertNotNull(exchangeRate)
//        val rateDefault = exchangeRate?.rates?.get("USD")!!
//        val rateTo = exchangeRate?.rates?.get("BRL")!!
//        val totalBed = 2
//        val value = ExchangeUtils.currencyConverter(dorm.pricePerBed.times(totalBed), rateDefault, rateTo)
//        val carts = arrayListOf(
//            Cart(dorm.id, "Bed dorm ${dorm.id}", dorm.pricePerBed, totalBed)
//        )
//
//        cartViewModel.setShoppingCart(carts)
//        cartViewModel.setExchangeRate("BRL")
//        assertEquals(cartViewModel.pairTotalCart.value?.second!!, value, 0.01)
//    }
//
//    @Test
//    fun checkShoppingAndConverterDollarToEuro() {
//        dormViewModel.listDorms()
//        dorms = dormViewModel.dorms.value
//        assertNotNull(dorms)
//        val dorm = dorms?.get(1)!!
//        assertNotNull(dorm)
//
//        cartViewModel.getExchangeRates()
//        exchangeRate = cartViewModel.exchangeRate
//        assertNotNull(exchangeRate)
//        val rateDefault = exchangeRate?.rates?.get("USD")!!
//        val rateTo = exchangeRate?.rates?.get("EUR")!!
//        val totalBed = 2
//        val value = ExchangeUtils.currencyConverter(dorm.pricePerBed.times(totalBed), rateDefault, rateTo)
//        val carts = arrayListOf(
//            Cart(dorm.id, "Bed dorm ${dorm.id}", dorm.pricePerBed, totalBed)
//        )
//        cartViewModel.setShoppingCart(carts)
//        cartViewModel.setExchangeRate("EUR")
//        assertEquals(cartViewModel.pairTotalCart.value?.second!!, value, 0.01)
//    }
}