package com.example.brianduffy.masevo;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by Gabriel on 10/30/2017.
 */
public class UserTests {
    public User tUser0;

    @Before
    public void setUp() throws Exception {
        HashSet<Integer> PublicIDs = new HashSet<>();
        HashSet<Integer> PrivateIDs = new HashSet<>();
       // tUser0 = new User("testemail@gmail.com", 10.10f, 20.20f, PublicIDs, PrivateIDs);
    }

    @Test
    public void populateMyPublicEventList() throws Exception {

    }

    @Test
    public void populateMyPrivateEventList() throws Exception {

    }

    @Test // Public Test
    public void joinEvent() throws Exception {
        setUp();
        Date startTime = new Date(0);
        Date endTime = new Date(84400000l);
//       // PublicEvent publicEvent = new PublicEvent(startTime, endTime, 2.0f, 3.0f,
//                "PublicTestEvent", "This is a public test event!", 10, "creator@gmail.com");
//        tUser0.joinEvent(publicEvent);
//        assertEquals("User email was not found in the attendeeList", publicEvent.attendeeList.contains(tUser0.emailAddress), true);
//        assertEquals("The Public Event was not found in the users public event list", tUser0.myPublicEventList.contains(publicEvent), true);

        // Add tests for event attributes
    }

    @Test // Private Test
    public void joinEvent1() throws Exception {
        setUp();
        Date startTime = new Date(0);
        Date endTime = new Date(84400000);
       // Server server = new Server("", "", "", "");
//        PrivateEvent privateEvent = new PrivateEvent(startTime, endTime, 2.0f, 3.0f, "PrivateTestEvent", "This is a private test event!", 10, "creator@gmail.com", server);
//        assertEquals("User was able to join the private event with the incorrect ID", tUser0.joinEvent(privateEvent, (privateEvent.eventID - 1)), false);
//        assertEquals("User was not able to join the private event using the correct ID", tUser0.joinEvent(privateEvent, privateEvent.eventID), true);
//
//        assertEquals("The user was not foudn in the attendeeList", privateEvent.attendeeList.contains(tUser0.emailAddress), true);
//        assertEquals("The Private Event was not foudn in the users private event list", tUser0.myPrivateEventList.contains(privateEvent), true);
    }

    @Test
    public void leaveEvent() throws Exception {

    }

    @Test
    public void leaveEvent1() throws Exception {

    }

    @Test
    public void addHostToEvent() throws Exception {

    }

    @Test
    public void addHostToEvent1() throws Exception {

    }

    @Test
    public void editEventTimes() throws Exception {

    }

    @Test
    public void editEventTimes1() throws Exception {

    }

    @Test
    public void editEventName() throws Exception {

    }

    @Test
    public void editEventName1() throws Exception {

    }

    @Test
    public void editEventDesc() throws Exception {

    }

    @Test
    public void editEventDesc1() throws Exception {

    }

    @Test
    public void editEventRadius() throws Exception {

    }

    @Test
    public void editEventRadius1() throws Exception {

    }

    @Test
    public void editEventLocation() throws Exception {

    }

    @Test
    public void editEventLocation1() throws Exception {

    }

    @Test
    public void editEventDisplayName() throws Exception {

    }

    @Test
    public void editEventDisplayName1() throws Exception {

    }

}