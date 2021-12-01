package org.apache.fineract.ui.online.tellers.create_teller

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.apache.fineract.R
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.online.teller.TellerAction
import org.apache.fineract.ui.online.teller.createteller.CreateTellerActivity
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.toDataClass
import org.junit.*
import org.junit.runner.RunWith


/**
 * Created by Varun Jain on 18/8/2021.
 */

@RunWith(AndroidJUnit4::class)
class CreateTellerActivityAndroidTest {

    @get:Rule
    var activityTestRule =
        ActivityTestRule<CreateTellerActivity>(CreateTellerActivity::class.java, false, false)

    lateinit var synchronizationManager: SynchronizationManager

    @Before
    fun before() {

        val intent = Intent().apply {
            putExtra(Constants.TELLER_ACTION, TellerAction.CREATE)
        }
        activityTestRule.launchActivity(intent)
        synchronizationManager =
            SynchronizationManager(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun isTellerListFragmentVisible() {
        onView(withId(R.id.slCreateTeller))
            .check(matches(isDisplayed()))
    }

    @Test
    fun createTeller() {

        // Filling all the details in Teller Details Step fragment
        onView(withId(R.id.etTellerCode))
            .perform(typeText("tellerCode"))
        onView(withId(R.id.etTellerPassword))
            .perform(typeText("teller Password"))
        onView(withId(R.id.etTellerCashdrawlimit))
            .perform(typeText("1000"))
        onView(withId(R.id.etTellerAccountIdentifier))
            .perform(typeText("teller Account Identifier"))
        onView(withId(R.id.etTellerVaultIdentifier))
            .perform(typeText("Teller Vault Identifier"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.cbTellerDenominationRequired))
            .check(matches(isNotChecked()))
            .perform(click())
        onView(withId(R.id.etTellerCheaque))
            .perform(typeText("Teller Cheques receivable amount"))
        onView(withId(R.id.scrollViewTellerDetailsStep))
            .perform(swipeUp())
        onView(withId(R.id.etTellerCashOverShortAccount))
            .perform(typeText("Cash over short Account"))
        onView(withId(R.id.etTellerAssignedEmployee))
            .perform(typeText("Assigned Employee"))
            .perform(closeSoftKeyboard())

        // Navigate to the next step
        onView(withText("NEXT")).perform(click())

        // Test if the Teller Review Step is populated with the correct values
        onView(withId(R.id.tvTellerReviewCode)).check(matches(withText("tellerCode")))
        onView(withId(R.id.tvTellerReviewPassword)).check(matches(withText("teller Password")))
        onView(withId(R.id.tvTellerReviewCashdrawLimit)).check(matches(withText("1000")))
        onView(withId(R.id.tvTellerReviewAccountIdentifier)).check(matches(withText("teller Account Identifier")))
        onView(withId(R.id.tvTellerReviewVaultAccountIdentifier)).check(matches(withText("Teller Vault Identifier")))
        onView(withId(R.id.tvTellerReviewDenomination)).check(matches(isChecked()))
        onView(withId(R.id.tvTellerReviewCra)).check(matches(withText("Teller Cheques receivable amount")))
        onView(withId(R.id.tvTellerReviewCashoverShortAccount)).check(matches(withText("Cash over short Account")))
        onView(withId(R.id.tvTellerReviewAssignedEmployee)).check(matches(withText("Assigned Employee")))

        // Complete creating the teller
        onView(withText("COMPLETE")).perform(click())

        // assert if the Teller has been created successfully
        val mapItem = synchronizationManager.getDocumentForTest(
            "teller Account Identifier",
            InstrumentationRegistry.getInstrumentation().context
        )

        val teller  = mapItem.toDataClass<Teller>()

        Assert.assertEquals(teller.code, "tellerCode")
        Assert.assertEquals(teller.password, "teller Password")
    }

    @After
    fun after() {
        //delete the created document in test
        synchronizationManager.deleteDocument(
            "testTeller"
        )
    }
}