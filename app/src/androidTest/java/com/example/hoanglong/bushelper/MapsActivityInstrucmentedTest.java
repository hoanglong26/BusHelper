package com.example.hoanglong.bushelper;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.AutoCompleteTextView;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Created by hoanglong on 02-Jul-17.
 */
@RunWith(AndroidJUnit4.class)
public class MapsActivityInstrucmentedTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    private MainActivity mMainActivity;

    @Before
    public void setup() {
        mMainActivity = mActivityRule.getActivity();
    }


    @Test
    public void inputValidThenPressGoWithNoRoute() {

        try {
            onView(withId(R.id.search)).perform(click());


            onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("new"));


            Thread.sleep(1000);


            onView(withText(containsString("New Zealand")))
                    .inRoot(withDecorView(not(Matchers.is(mMainActivity.getWindow().getDecorView()))))
                    .perform(click());


            Thread.sleep(1000);

            onView(withId(R.id.searchBtn)).perform(click());

            intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));
            Thread.sleep(3000);

            onView(withText(containsString("No route")))
                    .inRoot(withDecorView(not(mMainActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void clickOnMarkerThenRemoveFailed() {
        try {
            onView(withId(R.id.btnMylocation)).perform(click());
            Thread.sleep(1000);

            UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
            UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Your location"));

            mMarker1.click();
            Thread.sleep(1000);

            onView(withText(containsString("Remove marker")))
                    .inRoot(withDecorView(not(Matchers.is(mMainActivity.getWindow().getDecorView()))))
                    .perform(click());
            Thread.sleep(1500);

            onView(withText(R.string.cannot_remove_marker))
                    .inRoot(withDecorView(not(mMainActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));

        } catch (UiObjectNotFoundException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void clickOnMarkerThenRemoveSuccess() {
        try {
            onView(withId(R.id.search)).perform(click());


            onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("an lac"));


            Thread.sleep(1000);


            onView(withText(containsString("An Lac High School")))
                    .inRoot(withDecorView(not(Matchers.is(mMainActivity.getWindow().getDecorView()))))
                    .perform(click());


            Thread.sleep(1000);

            onView(withId(R.id.searchBtn)).perform(click());

            Thread.sleep(1000);

            UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
            UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Final stop"));

            mMarker1.click();
            Thread.sleep(1000);

            onView(withText(containsString("Remove marker")))
                    .inRoot(withDecorView(not(Matchers.is(mMainActivity.getWindow().getDecorView()))))
                    .perform(click());
            Thread.sleep(1500);

            onView(withText(R.string.marker_removed))
                    .inRoot(withDecorView(not(mMainActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));

        } catch (UiObjectNotFoundException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void clickOnMarkerThenAddToList() {
        try {
            onView(withId(R.id.btnMylocation)).perform(click());
            Thread.sleep(1000);

            UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
            UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Your location"));

            mMarker1.click();
            Thread.sleep(1000);

            onView(withText(containsString("Add to favorite")))
                    .inRoot(withDecorView(not(Matchers.is(mMainActivity.getWindow().getDecorView()))))
                    .perform(click());
            Thread.sleep(1000);

            onView(withText(containsString("list")))
                    .inRoot(withDecorView(not(mMainActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));

        } catch (UiObjectNotFoundException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
