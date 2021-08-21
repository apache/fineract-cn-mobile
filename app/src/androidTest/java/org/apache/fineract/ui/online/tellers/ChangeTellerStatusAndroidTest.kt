package org.apache.fineract.ui.online.tellers

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
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.adapters.TellerAdapter
import org.apache.fineract.ui.online.DashboardActivity
import org.apache.fineract.utils.toDataClass
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChangeTellerStatusAndroidTest {

    @get:Rule
    val activityRule = ActivityTestRule<DashboardActivity>(DashboardActivity::class.java)

    lateinit var synchronizationManager: SynchronizationManager

    @Before
    fun before() {
        synchronizationManager =
            SynchronizationManager(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testTellerTaskMgmtFeature() {

        //Open drawer
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .perform(DrawerActions.open())

        // Navigate to Teller list fragment
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.item_teller))

        //Click on teller item
        Espresso.onView(ViewMatchers.withId(R.id.rvTellers)).perform(
            RecyclerViewActions.actionOnItem<TellerAdapter.ViewHolder>(
                ViewMatchers.hasDescendant(ViewMatchers.withText("account identifier")),
                ViewActions.click()
            )
        )

        //Assert if thw item has been displayed correctly
        Espresso.onView(ViewMatchers.withId(R.id.tellerDetailsAccountIdentifier))
            .check(ViewAssertions.matches(ViewMatchers.withText("account identifier")))

        // Launch the Bottom sheet and click on the change Task Icon
        Espresso.onView(ViewMatchers.withId(R.id.llTellerDetailsTasks))
            .perform(ViewActions.click())

        // get current Teller model from the synchronization manager
        val mapItem = synchronizationManager.getDocumentForTest(
            "account identifier",
            InstrumentationRegistry.getInstrumentation().context
        )

        val teller  = mapItem.toDataClass<Teller>()
        // click on the icon to change the teller status
        Espresso.onView(ViewMatchers.withId(R.id.ivTellerTask))
            .perform(ViewActions.click())

        val prevStatus =  teller.state

        // confirm Task change operation
        Espresso.onView(ViewMatchers.withId(R.id.btnTellerSubmitTask))
            .perform(ViewActions.click())

        // assert if the Teller status was changed successfully
        when (prevStatus) {
            Teller.State.OPEN -> {
                Assert.assertEquals(teller.state, Teller.State.OPEN)
            }
            Teller.State.CLOSED -> {
                Assert.assertEquals(teller.state, Teller.State.CLOSED)
            }
        }
    }

}