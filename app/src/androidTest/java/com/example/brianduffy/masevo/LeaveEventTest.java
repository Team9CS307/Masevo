package com.example.brianduffy.masevo;

/**
 * Created by Brian Duffy on 11/30/2017.
 */
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;


import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Brian Duffy on 11/29/2017.
 */
@RunWith(AndroidJUnit4.class)

public class LeaveEventTest {

    @Rule
    public ActivityTestRule<MainActivity> mLoginTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testLeaveFailure() {
        onView(withId(R.id.event_name)).perform(typeText("event name8"));
        onView(withId(R.id.event_desc)).perform(typeText("description8"));
        onView(withId(R.id.start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(2017,12,12));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(12,12));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.end_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(2017,12,13));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(12,12));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.eventradius)).perform(replaceText("100"));
        MainActivity.user.myLocation.latitude = 40.429691f;
        MainActivity.user.myLocation.longitude = -86.913393f;
        onView(withId(R.id.create_event)).perform(click());
        onView(withText("event name8")).check(matches(withText("event name8")));
        onView(withText("description8")).check(matches(withText("description8")));
        onView(withText("event name8")).perform(longClick());
        onView(withText("Leave")).perform(click());
        onView(withText("event name8")).check(matches(withText("event name8")));

    }
}
