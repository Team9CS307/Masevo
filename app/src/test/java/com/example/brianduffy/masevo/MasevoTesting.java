package com.example.brianduffy.masevo;

import android.util.Pair;

import com.example.chambe41.masevo.ThreadCreateEvent;
import com.example.chambe41.masevo.ThreadDeleteEvent;

import junit.framework.Assert;

import org.junit.Test;

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
    public void deleteEventServerTest() {

    }

    @Test
    public void modifyEventServerTest() {

    }

    @Test
    public void removeUserServerTest() {

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
