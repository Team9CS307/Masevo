package com.example.brianduffy.masevo;

import java.util.HashSet;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public class User {
    String username;
    String emailAddress;
    HashSet<Integer> myEventList;
    Region myRegion;
    Location myLocation;
    HashSet<String> nearbyRegions;

    // Gets and sets a users Location
    public void getMyLocation()
    //public Location getMyLocation()
    {

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
    public Boolean joinEvent(PrivateEvent eventToJoin, String uniqueID)
    {
        // Check to see if the given uniqueID matches the events uniqueID
        if (uniqueID.equals(eventToJoin.uniqueID))
        {
            // Add user to the events list of attendees
            eventToJoin.attendeeList.add(username);
            // Add this event to the users list of Events
            myEventList.add(eventToJoin.eventID);
            return true;
        }
        return false;
    }

    // Allows a user to create a Public Event
    public void createEvent()
    {

    }
}
