package com.example.brianduffy.masevo;

import android.util.Pair;

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
    public PrivateEvent(String eventName, String eventDesc,Date startTime, Date endTime,
                        float latitude, float longitude, float radius, String creatorEmail)
    {
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

    public Pair<PrivateEvent,Integer> createEvent() {

        return null;

    }

    public Pair<Boolean,Integer> deleteEvent(int eventID) {

        return null;
    }

    @Override
    public boolean modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate, float latitude, float longitude, float radius, String hostEmail) {

        return false;
    }


    void modifyEvent(int eventID) {

    }

    @Override
    public Pair<Boolean,Integer> joinEvent(int eventID) {

        return null;
    }

    @Override
    public Pair<Boolean,Integer> leaveEvent(int eventID,String senderEmail) {

        return null;
    }

    public Pair<ArrayList<PublicEvent>, Integer> getEvents() {
        return null;
    }

    @Override
    public Pair<Boolean,Integer> addUser(int eventID, String email) {

        return null;
    }

    @Override
    public boolean removeUser(int eventID, String email) {

        return false;
    }

    @Override
    public boolean banUser(int eventID, String email) {

        return false;
    }

    @Override
    public boolean makeOwner(int eventID, String email) {

        return false;
    }

    @Override
    public boolean makeHost(int eventID, String email) {

        return false;
    }

    @Override
    public boolean makeUser(int eventID, String email) {

        return false;
    }
}
