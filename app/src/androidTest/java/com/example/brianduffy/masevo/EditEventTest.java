package com.example.brianduffy.masevo;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by Brian Duffy on 11/20/2017.
 */

@RunWith(AndroidJUnit4.class)
public class EditEventTest {
    @Rule
    public ActivityTestRule<MainActivity> mLoginTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Before
    public void createMockEvent() {
        onView(withId(R.id.event_name)).perform(typeText("event name5"));
        onView(withId(R.id.event_desc)).perform(typeText("description5"));
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
//        onView(withId(R.id.header)).check(matches(withText("event name")));
//        onView(withId(R.id.text)).check(matches(withText("description")));
    }

    @Test
    public void editEventSuccess() {

        onView(withId(R.id.header)).perform(longClick());
        onView(withText("Edit Event")).perform(click());
        onView(withId(R.id.event_name)).check(matches(withText("event name6")));
        onView(withId(R.id.event_desc)).check(matches(withText("description6")));
        onView(withId(R.id.start_time)).check(matches(withText("2017-12-12 12:12")));
        onView(withId(R.id.end_time)).check(matches(withText("2017-12-13 12:12")));
        onView(withId(R.id.event_type_text)).check(matches(withText("Public Event")));
        onView(withId(R.id.event_name)).perform(replaceText("New Event Name"));
        onView(withId(R.id.event_desc)).perform(replaceText("New Description"));

        onView(withId(R.id.start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(2018,11,11));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(11,11));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.eventradius)).perform(replaceText("100"));
        MainActivity.user.myLocation.latitude = 40.429691f;
        MainActivity.user.myLocation.longitude = -86.913393f;
        onView(withId(R.id.create_event)).perform(click());
        onView(withText("New Event Name")).check(matches(withText("New Event Name")));
        onView(withText("New Description")).check(matches(withText("New Description")));


    }
    @Test
    public void editEventFailure() {

        onView(withId(R.id.header)).perform(longClick());
        onView(withText("Edit Event")).perform(click());
        onView(withId(R.id.event_name)).check(matches(withText("event name7")));
        onView(withId(R.id.event_desc)).check(matches(withText("description7")));
        onView(withId(R.id.start_time)).check(matches(withText("2017-12-12 12:12")));
        onView(withId(R.id.end_time)).check(matches(withText("2017-12-13 12:12")));
        onView(withId(R.id.event_type_text)).check(matches(withText("Public Event")));
        onView(withId(R.id.event_name)).perform(replaceText("%4##"));
        onView(withId(R.id.event_desc)).perform(replaceText("New Description"));
        onView(withId(R.id.start_date)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(2018,11,11));
        onView(withText("Cancel")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(11,11));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.create_event)).perform(click());
        onView(withId(R.id.event_name_text)).check(matches(withText(R.string.invalid_eventname)));
        onView(withId(R.id.start_time)).check(matches(withText(R.string.invalid_datetext)));

        onView(withId(R.id.event_name)).perform(replaceText(""));
//        onView(withId(R.id.)).perform(click());
        onView(withText("Choose a start time")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(setDate(2017,12,12));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(setTime(12,12));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.create_event)).perform(click());
        onView(withId(R.id.event_name_text)).check(matches(withText(R.string.invalid_eventname2)));


    }
}
