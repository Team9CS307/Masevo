package com.example.brianduffy.masevo;

import android.util.Pair;

import com.example.chambe41.masevo.ThreadCreateEvent;
import com.example.chambe41.masevo.ThreadDeleteEvent;
import com.example.chambe41.masevo.ThreadJoinEvent;
import com.example.chambe41.masevo.ThreadModifyEvent;
import com.example.chambe41.masevo.ThreadRemoveFromActive;
import com.example.chambe41.masevo.ThreadRemoveUser;

import junit.framework.Assert;

import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;

/**
* Masevo Backend Unit Testing
*
* @author Vikram Pasumarti, vpasuma@purdue.edu
* @version 30 November 2017
*
*/

/*
ERRNO
-----
-1 - WHAT HAVE YOU DONE (UNKNOWN ERROR)
0 - SUCCESS NO ERROR
1 - EVENT ID NOT FOUND ERROR
2 - WRONG PERMISSIONS ERROR
3 - SET PUBLIC/PRIVATE ERROR
4 - NOT IN LIST ERROR
5 - ATTEMPT TO ADD A BANNED USER ERROR
6 - CURR USER ERROR
*/

public class MasevoTesting {
    Calendar temp = Calendar.getInstance();
    @Test
    public void createEventDeleteEventServerTest() throws InterruptedException {
        String eventName = "Sample Event";
        String eventDesc = "This is an Event Description";
        java.sql.Date startDate = new java.sql.Date(1543622400000l);
        java.sql.Date endDate = new java.sql.Date(1543626000000l);
        Location location = new Location(0,0);
        float radius = 10;
        int eventId = 0;
        String hostEmail = "testing.masevo";
        boolean pub = true;
        ThreadCreateEvent threadCreate = new ThreadCreateEvent(eventName, eventDesc, startDate, endDate, location, radius, eventId, hostEmail, pub);
        Thread createEventThread = new Thread(threadCreate);
        createEventThread.start();
        PublicEvent publicEvent = new PublicEvent(eventName,eventDesc,startDate,endDate,location.latitude,location.longitude,radius,hostEmail);
        Pair<PublicEvent, Integer> expected = new Pair<>(publicEvent,0);

        createEventThread.join();
        Pair<Event,Integer> actual = threadCreate.getReturnResult();

        Assert.assertEquals(expected.second,actual.second); //checks if event made successfully

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,"testing.masevo",true);
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();
        Pair<Boolean, Integer> expectedDel = threadDelete.getReturnResult();

        Assert.assertEquals((Integer) 0,expectedDel.second); //checks if event deleted successfully

    }

    @Test
    public void deleteEventPermissionsServerTest() throws InterruptedException {
        String eventName = "Sample Event";
        String eventDesc = "This is an Event Description";
        java.sql.Date startDate = new java.sql.Date(1543622400000l);
        java.sql.Date endDate = new java.sql.Date(1543626000000l);
        Location location = new Location(0,0);
        float radius = 10;
        int eventId = 0;
        String hostEmail = "testing.masevo";
        boolean pub = true;
        ThreadCreateEvent threadCreate = new ThreadCreateEvent(eventName, eventDesc, startDate, endDate, location, radius, eventId, hostEmail, pub);
        Thread createEventThread = new Thread(threadCreate);
        createEventThread.start();
        PublicEvent publicEvent = new PublicEvent(eventName,eventDesc,startDate,endDate,location.latitude,location.longitude,radius,hostEmail);
        Pair<PublicEvent, Integer> expected = new Pair<>(publicEvent,0);
        createEventThread.join();

        ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(0,"user", true);
        Thread joinEventThread = new Thread(threadJoinEvent);
        joinEventThread.start();
        joinEventThread.join();

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,"user",true);
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();
        Pair<Boolean, Integer> expectedDel = threadDelete.getReturnResult();

        Assert.assertEquals((Integer) 2,expectedDel.second); //checks if permissions error is thrown because normal users should not be able to delete events.

    }

    @Test
    public void modifyEventServerTest() throws InterruptedException {
        String eventName = "Sample Event";
        String eventDesc = "This is an Event Description";
        java.sql.Date startDate = new java.sql.Date(1543622400000l);
        java.sql.Date endDate = new java.sql.Date(1543626000000l);
        Location location = new Location(0,0);
        float radius = 10;
        int eventId = 0;
        String hostEmail = "testing.masevo";
        boolean pub = true;
        ThreadCreateEvent threadCreate = new ThreadCreateEvent(eventName, eventDesc, startDate, endDate, location, radius, eventId, hostEmail, pub);
        Thread createEventThread = new Thread(threadCreate);
        createEventThread.start();
        PublicEvent publicEvent = new PublicEvent(eventName,eventDesc,startDate,endDate,location.latitude,location.longitude,radius,hostEmail);
        Pair<PublicEvent, Integer> expected = new Pair<>(publicEvent,0);
        createEventThread.join();

        ThreadModifyEvent threadModifyEvent = new ThreadModifyEvent(eventId, eventName, eventDesc, startDate, endDate, location.latitude, location.longitude, radius, hostEmail, pub);
        Thread modifyEventThread = new Thread(threadModifyEvent);
        modifyEventThread.start();
        modifyEventThread.join();
        Pair<Event, Integer> actual = threadModifyEvent.getReturnResult();

        Assert.assertEquals((Integer) 0, actual.second); //checks if event is successfully modified

        ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(0,"user", true);
        Thread joinEventThread = new Thread(threadJoinEvent);
        joinEventThread.start();
        joinEventThread.join();

        ThreadModifyEvent threadModifyEvent2 = new ThreadModifyEvent(eventId, eventName, eventDesc, startDate, endDate, location.latitude, location.longitude, radius, "user", pub);
        Thread modifyEventThread2 = new Thread(threadModifyEvent2);
        modifyEventThread2.start();
        modifyEventThread2.join();
        Pair<Event, Integer> actualFail = threadModifyEvent.getReturnResult();

        Assert.assertEquals((Integer) 2, actualFail.second); //checks if permissions error is thrown because normal users should not be able to modify events.

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,hostEmail,true); //remove test event
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();

        //TODO: PUBLIC PRIVATE TESTING
    }

    @Test
    public void removeUserServerTest() throws InterruptedException {
        String eventName = "Sample Event";
        String eventDesc = "This is an Event Description";
        java.sql.Date startDate = new java.sql.Date(1543622400000l);
        java.sql.Date endDate = new java.sql.Date(1543626000000l);
        Location location = new Location(0,0);
        float radius = 10;
        int eventId = 0;
        String hostEmail = "testing.masevo";
        boolean pub = true;
        ThreadCreateEvent threadCreate = new ThreadCreateEvent(eventName, eventDesc, startDate, endDate, location, radius, eventId, hostEmail, pub);
        Thread createEventThread = new Thread(threadCreate);
        createEventThread.start();
        PublicEvent publicEvent = new PublicEvent(eventName,eventDesc,startDate,endDate,location.latitude,location.longitude,radius,hostEmail);
        Pair<PublicEvent, Integer> expected = new Pair<>(publicEvent,0);
        createEventThread.join();

        ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(0,"user", true);
        Thread joinEventThread = new Thread(threadJoinEvent);
        joinEventThread.start();
        joinEventThread.join();

        ThreadJoinEvent threadJoinEvent2 = new ThreadJoinEvent(0,"user2", true);
        Thread joinEventThread2 = new Thread(threadJoinEvent);
        joinEventThread2.start();
        joinEventThread2.join();


        ThreadRemoveUser threadRemoveUser = new ThreadRemoveUser(eventId, "user", "user", pub);
        Thread removeUserThread = new Thread(threadRemoveUser);
        removeUserThread.start();
        removeUserThread.join();
        Pair<Boolean,Integer> actual = threadRemoveUser.getReturnResult();

        Assert.assertEquals((Integer)10,actual.second); //checks if user tries to delete themself, should fail

        ThreadRemoveUser threadRemoveUser2 = new ThreadRemoveUser(eventId, "user", "user2", pub);
        Thread removeUserThread2 = new Thread(threadRemoveUser2);
        removeUserThread2.start();
        removeUserThread2.join();
        Pair<Boolean,Integer> actual2 = threadRemoveUser2.getReturnResult();

        Assert.assertEquals((Integer)9,actual2.second); //checks if user tries to delete another user, should fail

        ThreadRemoveUser threadRemoveUser3 = new ThreadRemoveUser(eventId, hostEmail, "userNOTHERE", pub);
        Thread removeUserThread3 = new Thread(threadRemoveUser3);
        removeUserThread3.start();
        removeUserThread3.join();
        Pair<Boolean,Integer> actual3 = threadRemoveUser3.getReturnResult();

        Assert.assertEquals((Integer)4,actual3.second); //checks if owner tries to delete a nonexistent user, should fail

        ThreadRemoveUser threadRemoveUser4 = new ThreadRemoveUser(eventId, hostEmail, "user", pub);
        Thread removeUserThread4 = new Thread(threadRemoveUser4);
        removeUserThread4.start();
        removeUserThread4.join();
        Pair<Boolean,Integer> actual4 = threadRemoveUser4.getReturnResult();

        Assert.assertEquals((Integer)4,actual4.second); //checks if owner tries to delete a user, should succeed

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,hostEmail,true); //remove test event
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();

    }

    @Test
    public void banUserServerTest() {

    }

    @Test
    public void makeOwnerServerTest() {

    }

    @Test
    public void makeHostServerTest() {

    }

    @Test
    public void makeUserServerTest() {

    }
}
