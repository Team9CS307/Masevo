package com.example.brianduffy.masevo;

import java.util.List;

/**
 * Created by Brian Duffy on 9/14/2017.
 */

public class User {
    String username;
    String emailAddress;
    List myEventList;
    Region myRegion;
    Location myLocation;
    List nearbyRegions;

    // Gets and sets a users Location
    public void getMyLocation()
    //public Location getMyLocation()
    {

    }

    // Allows a user to attempt to join a given Event
    public void joinEvent(Event eventToJoin)
    {

    }

    // Gets the Regions surrounding a given user
    public void getNearRegions()
    //public List<Region> getNearRegions()
    {

    }
}
