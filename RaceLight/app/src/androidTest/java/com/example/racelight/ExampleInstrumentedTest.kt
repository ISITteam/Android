package com.example.racelight

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.bluetooth.BluetoothClass
import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.StringStartsWith.startsWith
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.racelight", appContext.packageName)
    }

    @Test
    fun mainButtonsLoad() {
        Espresso.onView(withId(R.id.infoButton)).check(matches(withText("INSTRUCTIONS")))
        Espresso.onView(withId(R.id.startButton)).check(matches(withText("Start")))
        Espresso.onView(withId(R.id.rankingsButton)).check(matches(withText("Rankings")))
    }

    @Test
    fun instructionPageLoad() {

        Espresso.onView(withId(R.id.infoButton)).perform(click())
        Espresso.onView(withId(R.id.infoText)).check(matches(withText(startsWith("This"))))
        Espresso.onView(withId(R.id.backButton)).check(matches(withText("Home")))
        Espresso.onView(withId(R.id.backButton)).perform(click())
    }

    @Test
    fun startPageLoad() {

        Espresso.onView(withId(R.id.startButton)).perform(click())
        Espresso.onView(withId(R.id.launchButton)).check(matches(withText("Launch")))
        Espresso.onView(withId(R.id.backButton)).check(matches(withText("Home")))
        Espresso.onView(withId(R.id.backButton)).perform(click())
    }

    @Test
    fun rankingsPageLoad() {

        Espresso.onView(withId(R.id.rankingsButton)).perform(click())
        Espresso.onView(withId(R.id.rankingsText)).check(matches(withText(startsWith("Click"))))
        Espresso.onView(withId(R.id.backButton)).check(matches(withText("Home")))
        Espresso.onView(withId(R.id.backButton)).perform(click())
    }

    @Test
    fun startAnimationRun() {

        Espresso.onView(withId(R.id.startButton)).perform(click())
        Espresso.onView(withId(R.id.launchButton)).perform(click())
        Thread.sleep(4000)
        Espresso.onView(withId(R.id.backButton)).perform(click())
    }

    @Test
    fun rankingsPageDataLoad() {

        Espresso.onView(withId(R.id.rankingsButton)).perform(click())
        Espresso.onView(withId(R.id.showRanksButton)).perform(click())
        Thread.sleep(2000)
        Espresso.onView(withId(R.id.rankingsText)).check(matches(withText(startsWith("1."))))
    }




}
