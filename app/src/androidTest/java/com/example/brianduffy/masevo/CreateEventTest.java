package com.example.brianduffy.masevo;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Brian Duffy on 11/19/2017.
 */
@RunWith(AndroidJUnit4.class)

public class CreateEventTest {


    @Rule
    public ActivityTestRule<MainActivity> mLoginTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void createEvent() {
        onView(withId(R.id.event_name)).perform(typeText("event name1"));
        onView(withId(R.id.event_desc)).perform(typeText("description1"));
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
        onView(withText("event name1")).check(matches(withText("event name1")));
        onView(withText("description1")).check(matches(withText("description1")));

        //TODO now do the asserts *********************


        //onView(withId(R.id.end_date)).perform(click());


    }

    @Test
    public void createPrivateEvent() {
        onView(withId(R.id.event_name)).perform(typeText("event name2"));
        onView(withId(R.id.event_desc)).perform(typeText("description2"));
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
        onView(withId(R.id.event_type)).perform(click());
        onView(withId(R.id.create_event)).perform(click());

        onView(withText("event name2")).check(matches(withText("event name2")));
        onView(withText("description2")).check(matches(withText("description2")));
        //onView(withId(R.id.event_id)).check(matches(isDisplayed()));
    }

    @Test
    public void createEventFailTest() {
        onView(withId(R.id.event_name)).perform(typeText("&())@"));
        onView(withId(R.id.event_desc)).perform(typeText(""));
        onView(withId(R.id.start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform();
        onView(withText("Cancel")).perform(click());

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
        onView(withId(R.id.start_time)).check(matches(withText(R.string.invalid_datetext)));
        onView(withId(R.id.event_name_text)).check(matches(withText(R.string.invalid_eventname)));

    }
}
