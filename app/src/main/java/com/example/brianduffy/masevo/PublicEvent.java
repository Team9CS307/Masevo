package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PublicEvent extends Event{
    // Use this to create a PublicEvent

    public PublicEvent(Date startTime, Date endTime, float latitude, float longitude,
                       String eventName, String eventDesc, int radius, String creatorEmail,
                       Server server)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = new Location(latitude, longitude);
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.radius = radius;

        this.hostList = new HashSet<>();
        this.hostList.add(creatorEmail);
        this.attendeeList = new HashSet<>();
        this.attendeeList.add(creatorEmail);
        this.activeList = new HashSet<>();
        this.emailToDisplay = new HashMap<>();

        this.eventID = (eventName + creatorEmail + startTime + endTime + Double.toString(Math.random())).hashCode();

        // We now want to add this PublicEvent to the database using the proper call
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
        server.createPublicEvent(eventID, eventName, eventDesc, startTime, endTime,
                latitude, longitude, radius, new String[]{"10" + creatorEmail});
    }
}
