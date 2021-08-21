package org.apache.fineract.ui.online.accounts

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.apache.fineract.R
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.data.models.accounts.AccountType
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.online.accounting.accounts.AccountAction
import org.apache.fineract.ui.online.accounting.accounts.createaccount.CreateAccountActivity
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.toDataClass
import org.junit.*
import org.junit.runner.RunWith

/**
 * Created by Varun Jain on 21/8/2021.
 */

@RunWith(AndroidJUnit4::class)
class CreateAccountActivityAndroidTest {

    @get:Rule
    var activityTestRule =
        ActivityTestRule<CreateAccountActivity>(CreateAccountActivity::class.java, false, false)

    lateinit var synchronizationManager: SynchronizationManager

    @Before
    fun before() {

        val intent = Intent().apply {
            putExtra(Constants.ACCOUNT_ACTION, AccountAction.CREATE)
        }
        activityTestRule.launchActivity(intent)
        synchronizationManager =
            SynchronizationManager(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun isCreateAccountStepVisible() {
        onView(withId(R.id.slCreateAccount))
            .check(matches(isDisplayed()))
    }

    @Test
    fun createAccountTest() {

        // Filling all the details in Account Details Step fragment
        onView(withId(R.id.etIdentifierAccountDetailsStep))
            .perform(typeText("test account"))
        onView(withId(R.id.etNameAccountDetailsStep))
            .perform(typeText("test name"))
        onView(withId(R.id.etAltAccountNumAccountDetailsStep))
            .perform(typeText("alternate account number"))
        onView(withId(R.id.etLedgerAccountDetailsStep))
            .perform(typeText("test Ledger"))
        onView(withId(R.id.scrollViewAccountDetailsStep))
            .perform(swipeUp())
        onView(withId(R.id.etRefAccountNumAccountDetailsStep))
            .perform(typeText("test Reference account number"))
        onView(withId(R.id.etBalanceAccountDetailsStep))
            .perform(typeText("1000"))
            .perform(closeSoftKeyboard())

        // Navigate to the next step
        onView(withText("NEXT")).perform(click())

        // Create the Holders
        onView(withId(R.id.ivAddHolder))
            .perform(click())
        onView(withId(R.id.etNewHolder))
            .perform(typeText("Account"))
        onView(withId(R.id.scrollViewAddAccountHolderStep))
        onView(withId(R.id.btnAddHolder))
            .perform(click())

        // Edit an Account assignment
        onView(withId(R.id.iv_edit))
            .perform(click())
        onView(withId(R.id.etNewHolder))
            .perform(typeText(" test holder"))
        onView(withId(R.id.scrollViewAddAccountHolderStep))
            .perform(swipeUp())
        onView(withId(R.id.btnAddHolder))
            .perform(click())

        // Delete a Account assignment
        onView(withId(R.id.iv_delete))
            .perform(click())

        // Dismiss the Delete dialog
        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(pressBack());

        // Navigate to the next step
        onView(withText("NEXT")).perform(click())

        // Create the Signature Authorities
        onView(withId(R.id.ivAddSignatureAuthorities))
            .perform(click())
        onView(withId(R.id.etNewSignatureAuthorities))
            .perform(typeText("test signature authority"))
        onView(withId(R.id.scrollViewAddAccountSignatureAuthorityStep))
        onView(withId(R.id.btnAddSignatureAuthority))
            .perform(click())

        // Navigate to the next step
        onView(withText("NEXT")).perform(click())

        // Test if the account Review Step is populated with the correct values
        onView(withId(R.id.tvIdentifierAccountStepReview)).check(matches(withText("test account")))
        onView(withId(R.id.tvTypeAccountStepReview)).check(matches(withText(AccountType.ASSET.toString())))
        onView(withId(R.id.tvNameAccountStepReview)).check(matches(withText("test name")))
        onView(withId(R.id.scrollViewAccountReviewStep))
            .perform(swipeUp())
        onView(withId(R.id.rvHoldersAccountStepReview))
            .perform(
                RecyclerViewActions.actionOnItem<NameListAdapter.ViewHolder>(
                    hasDescendant(withText("Account test holder")), click()
                )
            )
        onView(withId(R.id.rvSignatureAuthoritiesAccountStepReview))
            .perform(
                RecyclerViewActions.actionOnItem<NameListAdapter.ViewHolder>(
                    hasDescendant(withText("test signature authority")), click()
                )
            )
        onView(withId(R.id.tvRefAccountStepReview)).check(matches(withText("test Reference account number")))
        onView(withId(R.id.tvLedgerAccountStepReview)).check(matches(withText("test Ledger")))
        onView(withId(R.id.tvAltAccountNoAccountStepReview)).check(matches(withText("alternate account number")))

        // Complete creating the account
        onView(withText("COMPLETE")).perform(click())

        // assert if the account was created successfully
        val mapItem = synchronizationManager.getDocumentForTest(
            "test account",
            InstrumentationRegistry.getInstrumentation().context
        )

        val account  = mapItem.toDataClass<Account>()

        Assert.assertEquals(account.identifier, "test account")
        Assert.assertEquals(account.name, "test name")
    }

    @After
    fun after() {
        //delete the created document in test
        synchronizationManager.deleteDocument(
            "test account"
        )
    }
}