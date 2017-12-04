package com.example.brianduffy.masevo;

import android.util.Pair;

import junit.framework.Assert;

import org.junit.Test;

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
6 - BANNED
7 - ALREADY IN EVENT
8 - NOT IN EVENT
9 - NOT OWNER
10 - LEAVE AS OWNER
11 - SELF ERROR (remove, add)
*/

public class MasevoTesting {

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
        //Pair<Event,Integer> actual = threadCreate.getReturnResult();
        return;
        /*
        Assert.assertEquals(expected.second,actual.second); //checks if event made successfully

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,"testing.masevo",true);
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();
        Pair<Boolean, Integer> expectedDel = threadDelete.getReturnResult();

        Assert.assertEquals((Integer) 0,expectedDel.second); //checks if event deleted successfully
        */
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
        Pair<Event, Integer> actualFail = threadModifyEvent2.getReturnResult();

        Assert.assertEquals((Integer) 2, actualFail.second); //checks if permissions error is thrown because normal users should not be able to modify events.

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,hostEmail,true); //remove test event
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();

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
        Thread joinEventThread2 = new Thread(threadJoinEvent2);
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
    public void banUserServerTest() throws InterruptedException {
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

        ThreadJoinEvent threadJoinEvent1 = new ThreadJoinEvent(0,"user2", true);
        Thread joinEventThread1 = new Thread(threadJoinEvent1);
        joinEventThread1.start();
        joinEventThread1.join();

        ThreadBanUser threadBanUser = new ThreadBanUser(eventId, "user", "user2",  true);
        Thread banUserThread = new Thread(threadBanUser);
        banUserThread.start();
        banUserThread.join();
        Pair<Boolean, Integer> actual = threadBanUser.getReturnResult();

        Assert.assertEquals((Integer) 2, actual.second); //checks if a user tries to ban another user, should fail

        ThreadBanUser threadBanUser2 = new ThreadBanUser(eventId, hostEmail, "user2",  true);
        Thread banUserThread2 = new Thread(threadBanUser2);
        banUserThread2.start();
        banUserThread2.join();
        Pair<Boolean, Integer> actual2 = threadBanUser2.getReturnResult();

        Assert.assertEquals((Integer) 0, actual2.second); //checks if owner tries banning a user, should succeed


    }

    @Test
    public void makeOwnerServerTest() throws InterruptedException {
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
        Thread joinEventThread2 = new Thread(threadJoinEvent2);
        joinEventThread2.start();
        joinEventThread2.join();

        ThreadMakeOwner threadMakeOwner = new ThreadMakeOwner(eventId, hostEmail, hostEmail,true);
        Thread makeOwnerThread = new Thread(threadMakeOwner);
        makeOwnerThread.start();
        makeOwnerThread.join();
        Pair<Boolean, Integer> actual = threadMakeOwner.getReturnResult();

        Assert.assertEquals((Integer) 10, actual.second); //checks if owner tries to make themself owner, should fail

        ThreadMakeOwner threadMakeOwner2 = new ThreadMakeOwner(eventId, "user", "user2",true);
        Thread makeOwnerThread2 = new Thread(threadMakeOwner2);
        makeOwnerThread2.start();
        makeOwnerThread2.join();
        Pair<Boolean, Integer> actual2 = threadMakeOwner2.getReturnResult();

        Assert.assertEquals((Integer) 2, actual2.second); //checks if user tries to make someone owner, should fail

        ThreadMakeOwner threadMakeOwner3 = new ThreadMakeOwner(eventId, hostEmail, "user",true);
        Thread  makeOwnerThread3 = new Thread(threadMakeOwner3);
        makeOwnerThread3.start();
        makeOwnerThread3.join();
        Pair<Boolean, Integer> actual3 =  threadMakeOwner3.getReturnResult();

        Assert.assertEquals((Integer) 0, actual3.second); //checks if creator promotes user, should succeed

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,hostEmail,true); //remove test event
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();
    }

    @Test
    public void makeHostServerTest() throws InterruptedException {
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

        ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(0,"userHost", true);
        Thread joinEventThread = new Thread(threadJoinEvent);
        joinEventThread.start();
        joinEventThread.join();

        ThreadJoinEvent threadJoinEvent2 = new ThreadJoinEvent(0,"user2", true);
        Thread joinEventThread2 = new Thread(threadJoinEvent2);
        joinEventThread2.start();
        joinEventThread2.join();

        ThreadJoinEvent threadJoinEvent3 = new ThreadJoinEvent(0,"user3", true);
        Thread joinEventThread3 = new Thread(threadJoinEvent3);
        joinEventThread3.start();
        joinEventThread3.join();

        ThreadMakeHost threadMakeHost = new ThreadMakeHost(eventId, hostEmail, "userHost",true);
        Thread makeHostThread = new Thread(threadMakeHost);
        makeHostThread.start();
        makeHostThread.join();
        Pair<Boolean, Integer> actual = threadMakeHost.getReturnResult();

        Assert.assertEquals((Integer) 0, actual.second); //checks if owner promoted user to host, should succeed

        ThreadMakeHost threadMakeHost2 = new ThreadMakeHost(eventId, "user2", "user3",true);
        Thread makeHostThread2 = new Thread(threadMakeHost2);
        makeHostThread2.start();
        makeHostThread2.join();
        Pair<Boolean, Integer> actual2 = threadMakeHost2.getReturnResult();

        Assert.assertEquals((Integer) 2, actual2.second); //checks if user tries to make someone host, should fail

        ThreadMakeHost threadMakeHost3 = new ThreadMakeHost(eventId, "userHost", "user",true);
        Thread makeHostThread3 = new Thread(threadMakeHost3);
        makeHostThread3.start();
        makeHostThread3.join();
        Pair<Boolean, Integer> actual3 = threadMakeHost3.getReturnResult();

        Assert.assertEquals((Integer) 2, actual3.second); //checks if host tries to promote user, should fail

        ThreadMakeHost threadMakeHost4 = new ThreadMakeHost(eventId, hostEmail, "userHost",true);
        Thread makeHostThread4 = new Thread(threadMakeHost4);
        makeHostThread4.start();
        makeHostThread4.join();
        Pair<Boolean, Integer> actual4 = threadMakeHost4.getReturnResult();

        Assert.assertEquals((Integer) 10, actual4.second); //checks if owner promoted host to host, should fail

        ThreadDeleteEvent threadDelete = new ThreadDeleteEvent(0,hostEmail,true); //remove test event
        Thread deleteEventThread = new Thread(threadDelete);
        deleteEventThread.start();
        deleteEventThread.join();

    }

    @Test
    public void makeUserServerTest() throws InterruptedException {
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
        Thread joinEventThread2 = new Thread(threadJoinEvent2);
        joinEventThread2.start();
        joinEventThread2.join();

        ThreadMakeHost threadMakeHost = new ThreadMakeHost(eventId, hostEmail, "user",true);
        Thread makeHostThread = new Thread(threadMakeHost);
        makeHostThread.start();
        makeHostThread.join();

        ThreadMakeUser threadMakeUser = new ThreadMakeUser(eventId,"user2", "user", true);
        Thread makeUserThread = new Thread(threadMakeUser);
        makeUserThread.start();
        makeUserThread.join();
        Pair<Boolean, Integer> actual = threadMakeUser.getReturnResult();

        Assert.assertEquals((Integer) 2, actual.second); //checks if host tries demoting someone to user, should fail

        ThreadMakeUser threadMakeUser2 = new ThreadMakeUser(eventId,"user", "user2", true);
        Thread makeUserThread2 = new Thread(threadMakeUser2);
        makeUserThread2.start();
        makeUserThread2.join();
        Pair<Boolean, Integer> actual2 = threadMakeUser2.getReturnResult();

        Assert.assertEquals((Integer) 2, actual2.second); //cannot demote a user to user

        ThreadMakeUser threadMakeUser3 = new ThreadMakeUser(eventId,hostEmail, "user", true);
        Thread makeUserThread3 = new Thread(threadMakeUser3);
        makeUserThread3.start();
        makeUserThread3.join();
        Pair<Boolean, Integer> actual3 = threadMakeUser3.getReturnResult();

        Assert.assertEquals((Integer) 0, actual3.second); //owner successully demotes a host to user
    }


}
