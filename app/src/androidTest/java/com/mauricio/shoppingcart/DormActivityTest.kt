package com.mauricio.shoppingcart


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mauricio.shoppingcart.dorms.view.DormActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.DEFAULT)
class DormActivityTest {

    @get:Rule
    val activityRule: ActivityScenarioRule<DormActivity>
            = ActivityScenarioRule(DormActivity::class.java)

    @Test
    fun dormActivityTest() {
        onView(withId(R.id.total_amount)).check(ViewAssertions.matches(ViewMatchers.withText("Hero: (0, 0)")))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.addBed),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_layout_item_photo),
                        childAtPosition(
                            withId(R.id.photo_recycler_view),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())
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
