package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PrivateEvent extends Event {
    // Use this to create a PrivateEvent
    public PrivateEvent(Date startTime, Date endTime, float latitude, float longitude,
                        String eventDesc, String eventName, float radius, String creatorEmail)
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

    public void createEvent() {}

    void deleteEvent(int eventID) {

    }

    @Override
    void modifyEvent(int eventID) {

    }

    @Override
    void joinEvent(int eventID) {

    }

    @Override
    void leaveEvent(int eventID) {

    }

    ArrayList<PrivateEvent> getEvents() {
        return null;
    }

    @Override
    void addUser(int eventID, String email) {

    }

    @Override
    void removeUser(int eventID, String email) {

    }

    @Override
    void banUser(int eventID, String email) {

    }

    @Override
    void makeOwner(int eventID, String email) {

    }

    @Override
    void makeHost(int eventID, String email) {

    }

    @Override
    void makeUser(int eventID, String email) {

    }
}
