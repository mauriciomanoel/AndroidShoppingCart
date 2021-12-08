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
import com.mauricio.shoppingcart.exchange.adapters.ExchangeRateRecyclerViewAdapter


@RunWith(AndroidJUnit4::class)
class CartActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(DormActivity::class.java)

    @Test
    fun checkAddTwoDormIn6BedDormAndOpenCartWithBrazilianReal() {

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

        try {
            Thread.sleep(1500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withId(R.id.total_amount_cart))
            .check(matches(withText("USD 35.12")))

        onView(withId(R.id.menu_change_currency)).perform(click())

        onView(withId(R.id.list_exchage_rate))
            .perform(
                RecyclerViewActions.actionOnItem<ExchangeRateRecyclerViewAdapter.ViewHolder>(hasDescendant(withText("BRL")), click()))

        onView(withId(R.id.total_amount_cart)).check(matches(withText("USD 35.12")))

        try { Thread.sleep(12500) } catch (e: InterruptedException) { e.printStackTrace() }
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
