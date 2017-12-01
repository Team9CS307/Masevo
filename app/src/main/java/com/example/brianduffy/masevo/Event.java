package com.example.brianduffy.masevo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

    abstract public boolean createEvent();
    abstract public boolean deleteEvent(int eventID);
    abstract public boolean modifyEvent(int eventID, String eventName, String eventDesc, Date startDate, Date endDate,
                            float latitude, float longitude, float radius, String hostEmail);
    abstract public boolean joinEvent(int eventID);
    abstract public boolean leaveEvent(int eventID);
    abstract public Map.Entry<Boolean, Map.Entry<ArrayList<PublicEvent>, String>> getEvents();

    abstract public boolean addUser (int eventID, String email);
    abstract public boolean removeUser (int eventID, String email);
    abstract public boolean banUser (int eventID, String email);
    abstract public boolean makeOwner(int eventID, String email);
    abstract public boolean makeHost(int eventID, String email);
    abstract public boolean makeUser(int eventID, String email);
}
