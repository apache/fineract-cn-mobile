package org.apache.fineract.ui.online.products

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.apache.fineract.R
import org.apache.fineract.ui.adapters.GroupsAdapter
import org.apache.fineract.ui.online.DashboardActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Varun Jain on 21/8/2021.
 */

@RunWith(AndroidJUnit4::class)
class ProductListFragmentAndroidTest {

    @get:Rule
    val activityRule =
        ActivityTestRule<DashboardActivity>(DashboardActivity::class.java)

    @Test
    fun openDrawerOpenProductListClickOnRecyclerViewItem() {

        //Open drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        //From NavigationView navigate to Products list screen
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.item_product))

        //Click on a recycler View item
        Espresso.onView(ViewMatchers.withId(R.id.rvProduct)).perform(
            RecyclerViewActions.actionOnItem<GroupsAdapter.ViewHolder>(
                ViewMatchers.hasDescendant(ViewMatchers.withText("product identifier")),
                ViewActions.click()
            )
        )

        //Assert if the item has been displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.tvIdentifierProduct))
            .check(ViewAssertions.matches(ViewMatchers.withText("product identifier")))
    }
}