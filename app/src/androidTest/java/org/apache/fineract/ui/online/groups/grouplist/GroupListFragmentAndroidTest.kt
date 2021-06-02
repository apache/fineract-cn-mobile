package org.apache.fineract.ui.online.groups.grouplist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.apache.fineract.R
import org.apache.fineract.ui.adapters.GroupsAdapter
import org.apache.fineract.ui.online.DashboardActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Ahmad Jawid Muhammadi on 24/8/20.
 */

@RunWith(AndroidJUnit4::class)
class GroupListFragmentAndroidTest {

    @get:Rule
    val activityRule =
            ActivityTestRule<DashboardActivity>(DashboardActivity::class.java)

    @Test
    fun openDrawer_OpenGroupList_ClickOnRecyclerViewItem() {

        //Open drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())

        //From NavigationView navigate to Group list screen
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_groups))

        //Click on group item
        onView(withId(R.id.rvGroups)).perform(
                RecyclerViewActions.actionOnItem<GroupsAdapter.ViewHolder>(
                        hasDescendant(withText("group")),
                        click()
                )
        )

        //Assert if thw item has been displayed correctly
        onView(withId(R.id.tvIdentifier))
                .check(matches(withText("group")))
    }

}