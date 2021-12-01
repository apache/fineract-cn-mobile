package org.apache.fineract.ui.online.products

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
import org.apache.fineract.data.models.product.InterestBasis
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.adapters.ProductAccountAssignmentsAdapter
import org.apache.fineract.ui.product.ProductAction
import org.apache.fineract.ui.product.createproduct.CreateProductActivity
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.toDataClass
import org.junit.*
import org.junit.runner.RunWith

/**
 * Created by Varun Jain on 21/8/2021.
 */

@RunWith(AndroidJUnit4::class)
class CreateEditProductAndroidTest {

    @get:Rule
    var activityTestRule =
        ActivityTestRule<CreateProductActivity>(CreateProductActivity::class.java, false, false)

    lateinit var synchronizationManager: SynchronizationManager

    @Before
    fun before() {

        val intent = Intent().apply {
            putExtra(Constants.PRODUCT_ACTION, ProductAction.CREATE)
        }

        activityTestRule.launchActivity(intent)

        synchronizationManager =
            SynchronizationManager(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun checkIfProductCreatedSuccessfully() {

        // Filling all the details in Product Details Step fragment
        onView(withId(R.id.etIdentifier))
            .perform(typeText("test identifier"))
        onView(withId(R.id.etName))
            .perform(typeText("test Name"))
        onView(withId(R.id.etPatternPackage))
            .perform(typeText("pattern package"))
        onView(withId(R.id.etDescription))
            .perform(typeText("product description"))
        onView(withId(R.id.etCurrencyCode))
            .perform(typeText("Currency code"))
        onView(withId(R.id.scrollViewProductDetailsStep))
            .perform(swipeUp())
        onView(withId(R.id.etParameters))
            .perform(typeText("Parameters"))
        onView(withId(R.id.etMinorCurrencyUnitDigits))
            .perform(typeText("1000"))
        onView(withId(R.id.etTemporalUnit))
            .perform(typeText("Temporal unit"))
        onView(withId(R.id.scrollViewProductDetailsStep))
            .perform(swipeUp())
        onView(withId(R.id.etTermRangeMax))
            .perform(typeText("1000"))
        onView(withId(R.id.etBalanceRangeMinimum))
            .perform(typeText("10"))
        onView(withId(R.id.etMaximumBalanceRange))
            .perform(typeText("1000"))
        onView(withId(R.id.scrollViewProductDetailsStep))
            .perform(swipeUp())
        onView(withId(R.id.etMinInterestRange))
            .perform(typeText("10"))
        onView(withId(R.id.etMaximumInterestRange))
            .perform(typeText("1000"))
            .perform(closeSoftKeyboard())

        // Navigate to the next step
        onView(withText("NEXT")).perform(click())

        // Create, editing and deleting the Account Assignments
        onView(withId(R.id.ibAddAccountAssignment))
            .perform(click())
        onView(withId(R.id.etDesignator))
            .perform(typeText("Desig"))
        onView(withId(R.id.etAccountIdentifier))
            .perform(typeText("Account Identifier"))
        onView(withId(R.id.scrollViewAddAccountAssignmentsStep))
            .perform(swipeUp())
        onView(withId(R.id.etLedgerIdentifier))
            .perform(typeText("Ledger Identifier"))
        onView(withId(R.id.btnAddProductAccountAssignment))
            .perform(click())

        // Edit an Account assignment
        onView(withId(R.id.ivEditProductAccountAss))
            .perform(click())
        onView(withId(R.id.etDesignator))
            .perform(typeText("nator"))
        onView(withId(R.id.scrollViewAddAccountAssignmentsStep))
            .perform(swipeUp())
        onView(withId(R.id.btnAddProductAccountAssignment))
            .perform(click())

        // Delete a Account assignment
        onView(withId(R.id.ivDeleteProductAccountAss))
            .perform(click())

        // Dismiss the Delete dialog
        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(pressBack());

        // Navigate to the next step
        onView(withText("NEXT")).perform(click())

        // Test if the Product Review Step is populated with the correct values
        onView(withId(R.id.tvProductIdentifier)).check(matches(withText("test identifier")))
        onView(withId(R.id.tvProductName)).check(matches(withText("test Name")))
        onView(withId(R.id.tvPatternPackageProduct)).check(matches(withText("pattern package")))
        onView(withId(R.id.tvProductDescription)).check(matches(withText("product description")))
        onView(withId(R.id.tvProductCurrencyCode)).check(matches(withText("Currency code")))
        onView(withId(R.id.tvProductMinorCurrencyUnit)).check(matches(withText("1000")))
        onView(withId(R.id.tvParameters)).check(matches(withText("Parameters")))
        onView(withId(R.id.tvInterestBasis)).check(matches(withText(InterestBasis.BEGINNING_BALANCE.toString())))
        onView(withId(R.id.scrollViewProductReviewStep))
            .perform(swipeUp())
        onView(withId(R.id.rvProductAccountAssignmentsStepReview))
            .perform(
                RecyclerViewActions.actionOnItem<ProductAccountAssignmentsAdapter.ViewHolder>(
                    hasDescendant(withText("Designator")), click()
                )
            )
        onView(withId(R.id.tvProductTemporalUnit)).check(matches(withText("Temporal unit")))

        // Complete defining the Product
        onView(withText("COMPLETE")).perform(click())

        // assert if the product has been created successfully
        val mapItem = synchronizationManager.getDocumentForTest(
            "test identifier",
            InstrumentationRegistry.getInstrumentation().context
        )

        val product = mapItem.toDataClass<Product>()

        Assert.assertEquals(product.name, "test Name")
        Assert.assertEquals(product.patternPackage, "pattern package")
    }

    @After
    fun after() {
        //delete the created document during the test
        synchronizationManager.deleteDocument(
            "test identifier"
        )
    }
}