package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;
import android.icu.text.StringPrepParseException;

import java.util.HashSet;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public abstract class Event {
    //ChatRoom chatRoom = new ChatRoom();
    // Need to implement ChatRoom
    public SimpleDateFormat startTime;
        // Start time of the event
    public SimpleDateFormat endTime;
        // End time of the event
    public Location location;
        // Location of the event
    public HashSet<String> hostList = new HashSet<>();
        // HashSet of hosts for the event
    public HashSet<String> attendeeList = new HashSet<>();
        // HashSet of attendees for the event
    public HashSet<String> activeList = new HashSet<>();
        // HashSet of all active members of the event
    public String eventDesc;
        // Description of the event
    //public MapView curEventMapView
        // Reference to the map for this event
    public String eventName;
        // String storing the display name of this event
    public String eventID;
        // String storing the ID of the event.
    public double radius;
        // Radius of the event as set by a host

    public Location getLocation()
    {
        return this.location;
    }
    public SimpleDateFormat getStartTime()
    {
        return this.startTime;
    }
    public SimpleDateFormat getEndTime()
    {
        return this.endTime;
    }
    public void setStartTime(SimpleDateFormat startTime)
    {
        this.startTime = startTime;
    }
    public void setEndTime(SimpleDateFormat endTime)
    {
        this.endTime = endTime;
    }
    public void setLocation(Location location)
    {
        this.location = location;
    }
}