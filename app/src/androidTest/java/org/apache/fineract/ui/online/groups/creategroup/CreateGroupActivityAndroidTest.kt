package org.apache.fineract.ui.online.groups.creategroup

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.apache.fineract.R
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.utils.Constants
import org.hamcrest.Matchers.not
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.apache.fineract.R
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.models.Group
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.toDataClass
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Ahmad Jawid Muhammadi on 30/8/20.
 */

@RunWith(AndroidJUnit4::class)
class CreateGroupActivityAndroidTest {

    @get:Rule
    var activityTestRule =
            ActivityTestRule<CreateGroupActivity>(CreateGroupActivity::class.java, false, false)


    lateinit var synchronizationManager: SynchronizationManager

    @Before
    fun before() {
        //Open CreateGroupActivity with an intent by putting  create group action as an extra
        val intent = Intent().apply {
            putExtra(Constants.GROUP_ACTION, GroupAction.CREATE)
        }
        activityTestRule.launchActivity(intent)
    }

    @Test
    fun testCreateGroupItem_allValidData() {
        synchronizationManager = SynchronizationManager(
                InstrumentationRegistry.getInstrumentation().context
        )
    }

    @Test
    fun createGroupItem() {
        onView(withId(R.id.etIdentifier))
                .perform(typeText("testIdentifier"))
        onView(withId(R.id.etGroupDefinitionIdentifier))
                .perform(typeText("group definition"))
        onView(withId(R.id.etName))
                .perform(typeText("group name"))
        onView(withId(R.id.etOffice))
                .perform(typeText("office name"))
        onView(withId(R.id.etAssignedEmployee))
                .perform(typeText("assignedEmployee"))

        //go to next fragment
        onView(withText("NEXT")).perform(click())

        //Add a member
        onView(withId(R.id.ibAddMember))
                .perform(click())
        onView(withId(R.id.etNewMember))
                .perform(typeText("Ahmad"))
        onView(withId((R.id.btnAddMember)))
                .perform(click())
        onView(withText("NEXT")).perform(click())

        //Add leader name
        onView(withId(R.id.ibAddLeader))
                .perform(click())
        onView(withId(R.id.etNewLeader))
                .perform(typeText("Jawid"))
        onView(withId((R.id.btnAddLeader)))
                .perform(click())
        onView(withText("NEXT")).perform(click())

        //fill address details
        onView(withId(R.id.etStreet))
                .perform(typeText("Street"))
        onView(withId(R.id.etCity))
                .perform(typeText("Pune"))
        onView(withId(R.id.etRegion))
                .perform(typeText("Region"))
        onView(withId(R.id.etPostalCode))
                .perform(typeText("411048"))
        onView(withId(R.id.etCountry))
                .perform(typeText("India"))
        onView(withText("NEXT")).perform(click())
        onView(withText("COMPLETE")).perform(click())

        //Check if the creating group error message has been displayed
        onView(withText("Error while creating group"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun testCreateGroupItem_invalidOfficeName() {
        onView(withId(R.id.etIdentifier))
                .perform(typeText("testIdentifier"))
        onView(withId(R.id.etGroupDefinitionIdentifier))
                .perform(typeText("group definition"))
        onView(withId(R.id.etName))
                .perform(typeText("group name"))
        onView(withId(R.id.etOffice))
                .perform(typeText("off"))
        onView(withId(R.id.etAssignedEmployee))
                .perform(typeText("assignedEmployee"))

        //go to next fragment
        onView(withText("NEXT")).perform(click())

        //Assert that the next fragment has not been displayed
        onView(withId(R.id.ibAddMember))
                .check(matches(not(isDisplayed())))
        onView(withId(R.id.tvAddedMember))
                .check(matches(not(isDisplayed())))
    }
}

        //Then assert if group item has been created
        val mapItem = synchronizationManager.getDocumentForTest(
                "testIdentifier",
                InstrumentationRegistry.getInstrumentation().context
        )
        val groupItem = mapItem.toDataClass<Group>()
        assertEquals(groupItem.identifier, "testIdentifier")
        assertEquals(groupItem.name, "group name")
    }

    @After
    fun after() {
        //delete the created document in test
        synchronizationManager.deleteDocument(
                "testIdentifier"
        )
    }
}
