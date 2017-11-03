package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;
import android.icu.text.StringPrepParseException;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public abstract class Event implements Serializable{
    //ChatRoom chatRoom = new ChatRoom();
    // Need to implement ChatRoom
    public Date startTime;
        // Start time of the event
    public Date endTime;
        // End time of the event
    public Location location;
        // Location of the event
    public HashSet<String> hostList = new HashSet<>();
        // HashSet of hosts for the event
    public HashSet<String> attendeeList = new HashSet<>();
        // HashSet of attendees for the event
    public HashSet<String> activeList = new HashSet<>();
        // HashSet of all active members of the event
    public HashMap<String, String> emailToDisplay = new HashMap<>();
        // HashMap relating a given email to a display name
    public String eventDesc;
        // Description of the event
    //public MapView curEventMapView
        // Reference to the map for this event
    public String eventName;
        // String storing the display name of this event
    public int eventID;
        // int storing the ID of the event.
    public float radius;
        // Radius of the event as set by a host
    public String host;
}
