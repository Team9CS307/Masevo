package com.example.brianduffy.masevo;

import java.util.HashSet;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public class User {
    String emailAddress;
    HashSet<Integer> myEventList;
    Location myLocation;

    public User(String emailAddress, float longitude, float latitude)
    {
        this.emailAddress = emailAddress;
        this.myLocation = new Location(longitude, latitude);
        this.myEventList = new HashSet<>();
    }


    // Allows a user to attempt to join a given Event
    public void joinEvent(PublicEvent eventToJoin)
    {

        // Add user to the events list of attendees
        eventToJoin.attendeeList.add(emailAddress);
        // Add this event to the users list of Events
        myEventList.add(eventToJoin.eventID);
    }
    // We will return true if the user has the correct uniqueID, false otherwise
    public Boolean joinEvent(PrivateEvent eventToJoin, int eventID)
    {
        // Check to see if the given uniqueID matches the events uniqueID
        if (eventID == eventToJoin.eventID)
        {
            // Add user to the events list of attendees
            eventToJoin.attendeeList.add(emailAddress);
            // Add this event to the users list of Events
            myEventList.add(eventToJoin.eventID);
            return true;
        }
        return false;
    }
}
