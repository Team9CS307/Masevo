package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PublicEvent extends Event implements Serializable{
    // Use this to create a PublicEvent

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PublicEvent(String eventName, String eventDesc, Date startTime, Date endTime,
                       float latitude, float longitude, float radius, String creatorEmail)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = new Location(latitude, longitude);
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.radius = radius;
        this.host = creatorEmail;

        this.hostList = new HashSet<>();
        this.attendeeList = new HashSet<>();
        this.activeList = new HashSet<>();
        this.emailToDisplay = new HashMap<>();

        this.eventID = (eventName + eventDesc + startTime + endTime + latitude + longitude +
                radius + Double.toString(Math.random())).hashCode();

        // We now want to add this PublicEvent to the database using the proper call
        ///////////////////////
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        //***DATABASE CALL***//
        ///////////////////////
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPublicEvent () {
        Server s = new Server();
        s.createPublicEvent(eventID, eventName, eventDesc, startTime, endTime,
                location.latitude, location.longitude, radius, host);
    }
}
