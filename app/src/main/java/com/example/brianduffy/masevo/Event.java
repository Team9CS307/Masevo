package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;
import android.icu.text.StringPrepParseException;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public abstract class Event implements Serializable{
    //ChatRoom chatRoom = new ChatRoom();
    // Need to implement ChatRoom
    public int eventID;
        // int storing the ID of the event.
    public String eventName;
        // String storing the display name of this event
    public String eventDesc;
        // Description of the event
    public Date startDate;
        // Start time of the event
    public Date endDate;
        // End time of the event
    public Location location;
        // Location of the event
    public float radius;
        // Radius of the event as set by a host
    //public MapView curEventMapView
    // Reference to the map for this event
    public String ownerEmail;
        //The owners email
    public HashSet<String> hostList = new HashSet<>();
        // HashSet of hosts for the event
    public HashSet<String> attendeeList = new HashSet<>();
        // HashSet of attendees for the event
    public HashSet<String> activeList = new HashSet<>();
        // HashSet of all active members of the event
    public HashMap<String, String> emailToDisplay = new HashMap<>();
        // HashMap relating a given email to a display name


    abstract void createEvent();
    abstract void deleteEvent(int eventID);
    abstract void modifyEvent(int eventID);
    abstract void joinEvent(int eventID);
    abstract void leaveEvent(int eventID);

    abstract ArrayList<? extends Event> getEvents ();

    abstract void addUser (int eventID, String email);
    abstract void removeUser (int eventID, String email);
    abstract void banUser (int eventID, String email);
    abstract void makeOwner(int eventID, String email);
    abstract void makeHost(int eventID, String email);
    abstract void makeUser(int eventID, String email);
}
