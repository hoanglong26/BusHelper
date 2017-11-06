package com.example.hoanglong.bushelper;

import android.content.ComponentName;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.AutoCompleteTextView;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
 * Created by hoanglong on 01-Jul-17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);


    private MainActivity mMainActivity;

    @Before
    public void setup() {
        mMainActivity = mActivityRule.getActivity();
    }

    @Test
    public void inputValidAndChooseThenPressGo() {

        try {
            onView(withId(R.id.search)).perform(click());


            onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("hung"));


            Thread.sleep(2500);


            onView(withText(containsString("Hung Vuong Plaza")))
                    .inRoot(withDecorView(not(Matchers.is(mMainActivity.getWindow().getDecorView()))))
                    .perform(click());


            Thread.sleep(1000);

            onView(withId(R.id.searchBtn)).perform(click());

            intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void inputNothingPressGo() {

        try {
            onView(withId(R.id.search)).perform(click());

            Thread.sleep(1000);

            onView(withId(R.id.searchBtn)).perform(click());

            onView(withText(R.string.please_choose_location))
                    .inRoot(withDecorView(not(mMainActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void inputInvalidPressGo() {

        try {
            onView(withId(R.id.search)).perform(click());


            onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("bla bla"));

            Thread.sleep(1000);

            onView(withId(R.id.searchBtn)).perform(click());

            onView(withText(R.string.please_choose_location))
                    .inRoot(withDecorView(not(mMainActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void clickOnRecyclerviewItem() {
        onView(withId(R.id.rvHistory))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));

    }

    @Test
    public void clickFloatingActionButton() {
        onView(withId(R.id.btnMylocation)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));

    }

}
