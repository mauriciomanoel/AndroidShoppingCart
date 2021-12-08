package com.mauricio.shoppingcart

import android.app.Instrumentation
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
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

@RunWith(AndroidJUnit4::class)
class DormActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(DormActivity::class.java)

    @Test
    fun checkEmptyShopping() {
        onView(withId(R.id.total_amount))
            .check(ViewAssertions.matches(withText("USD 0.00")))
    }

    @Test
    fun checkAddTwoDormIn6BedDorm() {
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

        onView(withId(R.id.total_amount))
            .check(ViewAssertions.matches(withText("USD 35.12")))

//
//        val appCompatImageView2 = onView(
//            allOf(
//                withId(R.id.addBed),
//                childAtPosition(
//                    allOf(
//                        withId(R.id.constraint_layout_item_photo),
//                        childAtPosition(
//                            withId(R.id.photo_recycler_view),
//                            2
//                        )
//                    ),
//                    5
//                ),
//                isDisplayed()
//            )
//        )
//        appCompatImageView2.perform(click())
//
//        val linearLayout = onView(
//            allOf(
//                withId(R.id.layout_bottom_sheet),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    1
//                ),
//                isDisplayed()
//            )
//        )
//        linearLayout.perform(click())

//        val linearLayout2 = onView(
//            allOf(
//                withId(R.id.layout_bottom_sheet),
//                childAtPosition(
//                    childAtPosition(
//                        withId(android.R.id.content),
//                        0
//                    ),
//                    1
//                ),
//                isDisplayed()
//            )
//        )
//        linearLayout2.perform(click())
    }

    @Test
    fun checkAddOneDormIn6BedDormAndOneDormIn12BedDorm() {

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
        ).perform(click())

        onView(
            allOf(
                withId(R.id.addBed),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_layout_item_dorm),
                        childAtPosition(
                            withId(R.id.dorm_recycler_view),
                            2
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(withId(R.id.total_amount))
            .check(ViewAssertions.matches(withText("USD 29.57")))

//        try {
//            Thread.sleep(12500)
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
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
