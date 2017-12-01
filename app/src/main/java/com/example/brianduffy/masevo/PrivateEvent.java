package com.example.brianduffy.masevo;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import com.example.chambe41.masevo.ThreadAddToActive;
import com.example.chambe41.masevo.ThreadAddUser;
import com.example.chambe41.masevo.ThreadBanUser;
import com.example.chambe41.masevo.ThreadCreateEvent;
import com.example.chambe41.masevo.ThreadDeleteEvent;
import com.example.chambe41.masevo.ThreadGetEvents;
import com.example.chambe41.masevo.ThreadJoinEvent;
import com.example.chambe41.masevo.ThreadLeaveEvent;
import com.example.chambe41.masevo.ThreadMakeHost;
import com.example.chambe41.masevo.ThreadMakeOwner;
import com.example.chambe41.masevo.ThreadMakeUser;
import com.example.chambe41.masevo.ThreadModifyEvent;
import com.example.chambe41.masevo.ThreadRemoveFromActive;
import com.example.chambe41.masevo.ThreadRemoveUser;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PrivateEvent extends Event {
    // Use this to create a PrivateEvent
    public PrivateEvent() {

    }

    public PrivateEvent(String eventName, String eventDesc, Date startTime, Date endTime,
                        float latitude, float longitude, float radius, String creatorEmail) {
        this.startDate = startTime;
        this.endDate = endTime;
        this.location = new Location(latitude, longitude);
        this.eventDesc = eventDesc;
        this.eventName = eventName;
        this.radius = radius;

        this.hostList = new HashSet<>();
        this.hostList.add(creatorEmail);
        this.attendeeList = new HashSet<>();
        this.attendeeList.add(creatorEmail);
        this.activeList = new HashSet<>();
        this.emailToDisplay = new HashMap<>();


        this.eventID = (eventName + creatorEmail + startTime + endTime + Double.toString(Math.random())).hashCode();

        // We now want to add this PrivateEvent to the database using the proper call
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
        //s.createPrivateEvent(eventID, eventName, eventDesc, startTime, endTime,
        //        latitude, longitude, radius, creatorEmail);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Pair<Event, Integer> createEvent(String eventName,String eventDesc,java.sql.Date startDate,java.sql.Date endDate,
                                            Location location, float radius,int eventID, String hostEmail,boolean pub) {
        ThreadCreateEvent threadCreateEvent = new ThreadCreateEvent(eventName,eventDesc,startDate,
                endDate,location,radius,eventID,hostEmail,false);
        new Thread(threadCreateEvent).start();

        return threadCreateEvent.getReturnResult();
    }

    @Override
    public Pair<Boolean, Integer> removeFromActive(int eventID, String email) {
        ThreadRemoveFromActive threadRemoveFromActive = new ThreadRemoveFromActive(eventID,email,false);
        new Thread(threadRemoveFromActive).start();
        return threadRemoveFromActive.getReturnResult();
    }

    @Override
    public Pair<Boolean, Integer> addToActive(int eventID, String email) {
        ThreadAddToActive threadAddToActive = new ThreadAddToActive(eventID,email,false);
        new Thread(threadAddToActive).start();
        return threadAddToActive.getReturnResult();
    }

    @Override
    public Pair<ArrayList<? extends Event>, Integer> getEvents() {
        ThreadGetEvents threadGetEvents = new ThreadGetEvents();
        new Thread(threadGetEvents).start();
        return threadGetEvents.getReturnResult();
    }

    @Override
    public Pair<Boolean,Integer> deleteEvent(int eventID, String email) {
        ThreadDeleteEvent threadDeleteEvent = new ThreadDeleteEvent(eventID,email,false);
        new Thread(threadDeleteEvent).start();


        return threadDeleteEvent.getReturnResult();
    }


    @Override
    public Pair<Boolean, Integer> joinEvent(int eventID, String senderEmail) {
        ThreadJoinEvent threadJoinEvent = new ThreadJoinEvent(eventID, senderEmail, false);
        new Thread(threadJoinEvent).start();
        return threadJoinEvent.getReturnResult();
    }

    //@Override
    @Override
    public Pair<Boolean, Integer> leaveEvent(int eventID, String senderEmail) {
        ThreadLeaveEvent threadLeaveEvent = new ThreadLeaveEvent(eventID, senderEmail, false);
        new Thread(threadLeaveEvent).start();
        return threadLeaveEvent.getReturnResult();
    }

    @Override
    public Pair<Boolean, Integer> addUser(int eventID, String email, String target) {
        ThreadAddUser threadAddUser = new ThreadAddUser(eventID, email, target, false);
        new Thread(threadAddUser).start();
        return threadAddUser.getReturnResult();

    }

    @Override
    public Pair<Boolean, Integer> removeUser(int eventID, String email, String target) {
        ThreadRemoveUser threadRemoveUser = new ThreadRemoveUser(eventID, email, target, false);
        new Thread(threadRemoveUser).start();
        return threadRemoveUser.getReturnResult();

    }

    @Override
    public Pair<Boolean, Integer> banUser(int eventID, String email, String target) {
        ThreadBanUser threadBanUser = new ThreadBanUser(eventID, email, target, false);
        new Thread(threadBanUser).start();
        return threadBanUser.getReturnResult();

    }

    @Override
    public Pair<Boolean, Integer> makeOwner(int eventID, String email, String target) {
        ThreadMakeOwner threadMakeOwner = new ThreadMakeOwner(eventID, email, target, false);
        new Thread(threadMakeOwner).start();
        return threadMakeOwner.getReturnResult();

    }

    @Override
    public Pair<Boolean, Integer> makeHost(int eventID, String email, String target) {
        ThreadMakeHost threadMakeHost = new ThreadMakeHost(eventID, email, target, false);
        new Thread(threadMakeHost).start();
        return threadMakeHost.getReturnResult();

    }

    @Override
    public Pair<Boolean, Integer> makeUser(int eventID, String email, String target) {
        ThreadMakeUser threadMakeUser = new ThreadMakeUser(eventID, email, target, false);
        new Thread(threadMakeUser).start();

        return threadMakeUser.getReturnResult();
    }

    @Override
    public Pair<Event, Integer> modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate,
                                            float latitude, float longitude, float radius, String hostEmail) {
        ThreadModifyEvent threadModifyEvent = new ThreadModifyEvent(eventID, eventName, eventDesc,
                startDate, endDate, latitude, longitude, radius, hostEmail, false);
        new Thread(threadModifyEvent).start();
        return threadModifyEvent.getReturnResult();
    }
}

