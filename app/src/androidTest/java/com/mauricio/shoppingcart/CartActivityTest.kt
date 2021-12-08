package com.mauricio.shoppingcart

import android.app.Instrumentation
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mauricio.shoppingcart.dorms.view.DormActivity
import org.hamcrest.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import com.mauricio.shoppingcart.cart.models.Currency
import com.mauricio.shoppingcart.cart.view.CartActivity
import com.mauricio.shoppingcart.dorms.models.Dorm
import com.mauricio.shoppingcart.exchange.adapters.ExchangeRateRecyclerViewAdapter
import com.mauricio.shoppingcart.exchange.models.ExchangeRate
import com.mauricio.shoppingcart.utils.exchange.ExchangeUtils
import com.mauricio.shoppingcart.utils.number.NumberUtils
import junit.framework.TestCase
import junit.framework.TestCase.assertNotNull
import java.util.*
import kotlin.collections.ArrayList


@RunWith(AndroidJUnit4::class)
class CartActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(DormActivity::class.java)

    companion object {
        var dorms: ArrayList<Dorm>? = null
        var currencies: ArrayList<Currency>? = null
    }

    @Before
    fun waitForTime() {
        Thread.sleep(1500)
        activityRule.scenario.onActivity {
            dorms = it.viewModel.dorms.value
        }
        Thread.sleep(500)
    }

    @Test
    fun checkAddTwoDormIn6BedDormAndOpenCartWithBrazilianRealCurrency() {
        val dorm = dorms?.get(0)
        var rateDefault: Double?
        var rateTo: Double?
        val exchangeRate = AndroidShoppingCartApplication.exchangeRate
        assertNotNull(exchangeRate)
        assertNotNull(dorm)

        rateDefault = exchangeRate?.rates?.get("USD")!!
        rateTo = exchangeRate?.rates?.get("BRL")!!
        val totalBed = 2
        val value = ExchangeUtils.currencyConverter(dorm?.pricePerBed?.times(totalBed), rateDefault, rateTo)
        val valueFormatted = NumberUtils.formatNumber(value, "pt_BR")

        onView(
            allOf(
                withId(R.id.addBed),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_layout_item_dorm),
                        childAtPosition(
                            withId(R.id.dorm_recycler_view),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        ).perform(click()).perform(click())

        onView(withId(R.id.layout_bottom_sheet_dorm)).perform(forceClick())

        try { Thread.sleep(500) } catch (e: InterruptedException) { e.printStackTrace() }

        onView(withId(R.id.checkout_shopping)).perform(forceClick())

        Thread.sleep(1500)

        onView(withId(R.id.total_amount_cart))
            .check(matches(withText(NumberUtils.formatNumber(dorm?.pricePerBed?.times(totalBed)))))

        onView(withId(R.id.menu_change_currency)).perform(click())

        onView(withId(R.id.list_exchage_rate))
            .perform(
                RecyclerViewActions.actionOnItem<ExchangeRateRecyclerViewAdapter.ViewHolder>(hasDescendant(withText("BRL")), click()))

        onView(withId(R.id.total_amount_cart)).check(matches(withText(valueFormatted)))

//        try { Thread.sleep(12500) } catch (e: InterruptedException) { e.printStackTrace() }
    }

    fun forceClick(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints() = allOf(isClickable(), isEnabled(), isDisplayed())
            override fun getDescription() = "force click"
            override fun perform(uiController: UiController, view: View) {
                view.performClick() // perform click without checking view coordinates.
                uiController.loopMainThreadUntilIdle()
            }
        }
    }
    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }


}
