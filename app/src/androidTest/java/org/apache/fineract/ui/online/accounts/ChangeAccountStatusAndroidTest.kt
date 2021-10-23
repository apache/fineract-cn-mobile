package org.apache.fineract.ui.online.accounts

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.apache.fineract.R
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.adapters.AccountsAdapter
import org.apache.fineract.ui.online.DashboardActivity
import org.apache.fineract.utils.toDataClass
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Varun Jain on 21/8/2021.
 */

@RunWith(AndroidJUnit4::class)
class ChangeAccountStatusAndroidTest {

    @get:Rule
    val activityRule = ActivityTestRule<DashboardActivity>(DashboardActivity::class.java)

    lateinit var synchronizationManager: SynchronizationManager

    @Before
    fun before() {
        synchronizationManager =
            SynchronizationManager(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testIfAccountStatusChangedSuccessfully() {

        //Open drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        // Navigate to Account list fragment
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.item_accounts))

        //Click on recyclerView item
        Espresso.onView(ViewMatchers.withId(R.id.rvAccount)).perform(
            RecyclerViewActions.actionOnItem<AccountsAdapter.ViewHolder>(
                ViewMatchers.hasDescendant(ViewMatchers.withText("account identifier")),
                ViewActions.click()
            )
        )

        //Assert if the item has been displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.tvIdentifierAccount))
            .check(ViewAssertions.matches(ViewMatchers.withText("account identifier")))

        // Launch the Bottom sheet and click on the change Task Icon
        Espresso.onView(ViewMatchers.withId(R.id.llTasksAccountDetails))
            .perform(ViewActions.click())

        // get current Account model from the synchronization manager
        val mapItem = synchronizationManager.getDocumentForTest(
            "account identifier",
            InstrumentationRegistry.getInstrumentation().context
        )

        val account  = mapItem.toDataClass<Account>()
        val prevStatus =  account.state

        // click on the icon to change the account status
        Espresso.onView(ViewMatchers.withId(R.id.ivAccountTask))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.etCommentAccountTasks))
            .perform(ViewActions.typeText("Changing state of this account"))
            .perform(ViewActions.closeSoftKeyboard())

        // confirm Task change operation
        Espresso.onView(ViewMatchers.withId(R.id.btnSubmitAccountTask))
            .perform(ViewActions.click())

        // assert if the account status was changed successfully
        when (prevStatus) {
            Account.State.LOCKED -> {
                Assert.assertEquals(account.state, Account.State.LOCKED)
            }
            Account.State.OPEN -> {
                Assert.assertEquals(account.state, Account.State.OPEN)
            }
        }
    }

}