package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;

import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PublicEvent extends Event{
    public PublicEvent(SimpleDateFormat startTime, SimpleDateFormat endTime, Location location,
                       String eventDesc, String eventName, double radius, String creatorEmail)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.eventDesc = eventDesc;
        this.eventName = eventName;
        this.radius = radius;

        this.hostList = new HashSet<>();
        this.hostList.add(creatorEmail);
        this.attendeeList = new HashSet<>();
        this.attendeeList.add(creatorEmail);
        this.activeList = new HashSet<>();

        // Generate eventID?
        //this.eventID = hashCode(location.);
    }
}
