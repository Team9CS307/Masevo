package com.example.brianduffy.masevo;

import android.util.Pair;

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
    public String hostEmail;
        //The host's email
    public HashSet<String> hostList = new HashSet<>();
        // HashSet of hosts for the event
    public HashSet<String> attendeeList = new HashSet<>();
        // HashSet of attendees for the event
    public HashSet<String> activeList = new HashSet<>();
        // HashSet of all active members of the event
    public HashMap<String, String> emailToDisplay = new HashMap<>();
        // HashMap relating a given email to a display name
    public EventUsers eventUsers = new EventUsers(new ArrayList<String>(),new ArrayList<Permission>(),new ArrayList<Boolean>());


    abstract public Pair<? extends Event,Integer> createEvent(String eventName,String eventDesc,
                                                              java.sql.Date startDate,java.sql.Date endDate,
                                                              Location location, float radius,int eventID,
                                                              String hostEmail,boolean pub);
    abstract public Pair<Boolean,Integer> deleteEvent(int eventID, String email);
    abstract public Pair<Event, Integer> modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate,
                                                     float latitude, float longitude, float radius, String hostEmail);
    abstract public Pair<Boolean,Integer> joinEvent(int eventID,String senderEmail);
    abstract public Pair<Boolean, Integer> leaveEvent(int eventID, String senderEmail);
    abstract public Pair<ArrayList<? extends Event>, Integer> getEvents();

    abstract public Pair<Boolean,Integer> addUser (int eventID, String email,String target);
    abstract public Pair<Boolean,Integer> removeUser (int eventID, String email,String target);
    abstract public Pair<Boolean,Integer> banUser (int eventID, String email, String target);
    abstract public Pair<Boolean,Integer> makeOwner(int eventID, String email, String target);
    abstract public Pair<Boolean,Integer> makeHost(int eventID, String email, String target);
    abstract public Pair<Boolean,Integer> makeUser(int eventID, String email, String target);
}
