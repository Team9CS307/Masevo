package com.example.brianduffy.masevo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

    public boolean createEvent() {

        return false;

    }

    public boolean deleteEvent(int eventID) {

        return false;
    }

    @Override
    public boolean modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate, float latitude, float longitude, float radius, String hostEmail) {

        return false;
    }


    void modifyEvent(int eventID) {

    }

    @Override
    public boolean joinEvent(int eventID) {

        return false;
    }

    @Override
    public boolean leaveEvent(int eventID) {

        return false;
    }

    Map.Entry<Boolean, Map.Entry<ArrayList<PublicEvent>, String>> getEvents() {
        return null;
    }

    @Override
    public boolean addUser(int eventID, String email) {

        return false;
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
