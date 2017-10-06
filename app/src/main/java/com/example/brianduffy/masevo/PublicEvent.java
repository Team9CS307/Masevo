package com.example.brianduffy.masevo;

import android.icu.text.SimpleDateFormat;

import java.sql.Date;
import java.util.HashSet;

/**
 * Created by Gabriel on 9/24/2017.
 */

public class PublicEvent extends Event{
    public PublicEvent(Date startTime, Date endTime, float latitude, float longitude,
                       String eventDesc, String eventName, double radius, String creatorEmail)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = new Location(latitude, longitude);
        this.eventDesc = eventDesc;
        this.eventName = eventName;
        this.radius = radius;

        this.hostList = new HashSet<>();
        this.hostList.add(creatorEmail);
        this.attendeeList = new HashSet<>();
        this.attendeeList.add(creatorEmail);
        this.activeList = new HashSet<>();

        // Generate eventID?
        this.eventID = hashCode();
    }
}
